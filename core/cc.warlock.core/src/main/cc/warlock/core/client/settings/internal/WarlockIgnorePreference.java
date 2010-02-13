package cc.warlock.core.client.settings.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockPattern;
import cc.warlock.core.client.internal.WarlockPattern;
import cc.warlock.core.client.internal.WarlockPreference;

public class WarlockIgnorePreference {
	public static WarlockPreference<Collection<IWarlockPattern>> getAll(WarlockClientPreferences prefs) {
		ArrayList<IWarlockPattern> results = new ArrayList<IWarlockPattern>();

		Preferences inode = prefs.getClientPreferences().node("ignore");
		try {
			for (String name : inode.childrenNames()) {
				results.add(getIgnore(inode.node(name)));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return new WarlockPreference<Collection<IWarlockPattern>>(prefs, inode.absolutePath(), results);
	}
	
	protected static IWarlockPattern getIgnore(Preferences node) {
		String text = node.get("text", null);
		boolean literal = node.getBoolean("literal", true);
		boolean caseInsensitive = node.getBoolean("case-insensitive", false);
		boolean wholeWord = node.getBoolean("whole-word", false);
		
		return new WarlockPattern(text, literal, caseInsensitive, wholeWord);
	}
}
