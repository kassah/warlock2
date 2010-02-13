package cc.warlock.core.client.settings;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockPattern;
import cc.warlock.core.client.internal.WarlockPattern;

public class WarlockTriggerProvider implements WarlockPreferenceProvider<IWarlockPattern> {
	private static WarlockTriggerProvider instance;
	
	private WarlockTriggerProvider() { }
	
	protected static synchronized WarlockTriggerProvider getInstance() {
		if(instance == null)
			instance = new WarlockTriggerProvider();
		return instance;
	}
	
	public static Collection<WarlockPreference<IWarlockPattern>> getAll(WarlockClientPreferences prefs) {
		ArrayList<WarlockPreference<IWarlockPattern>> results =
			new ArrayList<WarlockPreference<IWarlockPattern>>();

		Preferences inode = prefs.getNode().node("trigger");
		try {
			for (String name : inode.childrenNames()) {
				Preferences node = inode.node(name);
				results.add(new WarlockPreference<IWarlockPattern>(getInstance(),
						node.absolutePath(), getPattern(node)));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return results;
	}
	
	protected static IWarlockPattern getPattern(Preferences node) {
		String text = node.get("text", null);
		boolean literal = node.getBoolean("literal", true);
		boolean caseInsensitive = node.getBoolean("case-insensitive", false);
		boolean wholeWord = node.getBoolean("whole-word", false);
		
		return new WarlockPattern(text, literal, caseInsensitive, wholeWord);
	}
	
	protected static void saveTrigger(Preferences node, IWarlockPattern pattern) {
		node.put("text", pattern.getText());
		node.putBoolean("literal", pattern.isLiteral());
		node.putBoolean("case-insensitive", pattern.isCaseInsensitive());
		node.putBoolean("whole-word", pattern.isWholeWord());
	}
	
	public void save(String path, IWarlockPattern value) {
		saveTrigger(WarlockPreferences.getScope().getNode(path), value);
	}
}
