package cc.warlock.core.client.settings;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockPattern;
import cc.warlock.core.client.internal.WarlockPattern;

public class WarlockIgnoreProvider implements WarlockPreferenceProvider<IWarlockPattern> {
	private static final WarlockIgnoreProvider instance = new WarlockIgnoreProvider();
	
	private WarlockIgnoreProvider() { }
	
	public static WarlockIgnoreProvider getInstance() {
		return instance;
	}
	
	public static Collection<WarlockPreference<IWarlockPattern>> getAll(WarlockClientPreferences prefs) {
		ArrayList<WarlockPreference<IWarlockPattern>> results =
			new ArrayList<WarlockPreference<IWarlockPattern>>();

		Preferences inode = prefs.getNode().node("ignore");
		try {
			for (String name : inode.childrenNames()) {
				Preferences node = inode.node(name);
				results.add(new WarlockPreference<IWarlockPattern>(getInstance(),
						node.absolutePath(), getIgnore(node)));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return results;
	}
	
	protected static IWarlockPattern getIgnore(Preferences node) {
		String text = node.get("text", null);
		boolean literal = node.getBoolean("literal", true);
		boolean caseInsensitive = node.getBoolean("case-insensitive", false);
		boolean wholeWord = node.getBoolean("whole-word", false);
		
		return new WarlockPattern(text, literal, caseInsensitive, wholeWord);
	}
	
	protected static void saveIgnore(Preferences node, IWarlockPattern pattern) {
		node.put("text", pattern.getText());
		node.putBoolean("literal", pattern.isLiteral());
		node.putBoolean("case-insensitive", pattern.isCaseInsensitive());
		node.putBoolean("whole-word", pattern.isWholeWord());
	}
	
	public void save(String path, IWarlockPattern value) {
		saveIgnore(WarlockPreferences.getScope().getNode(path), value);
	}
}
