package cc.warlock.core.client.settings.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockHighlight;
import cc.warlock.core.client.internal.WarlockPreference;

public class WarlockHighlightPreference {
	public static WarlockPreference<Collection<IWarlockHighlight>> getAll(WarlockClientPreferences prefs) {
		ArrayList<IWarlockHighlight> results = new ArrayList<IWarlockHighlight>();

		Preferences hnode = prefs.getClientPreferences().node("highlight");
		try {
			for (String name : hnode.childrenNames()) {
				results.add(getHighlight(hnode.node(name)));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return new WarlockPreference<Collection<IWarlockHighlight>>(prefs, hnode.absolutePath(), results);
	}
	
	protected static IWarlockHighlight getHighlight(Preferences node) {
		String text = node.get("text", null);
		boolean literal = node.getBoolean("literal", true);
		boolean caseInsensitive = node.getBoolean("case-insensitive", false);
		boolean wholeWord = node.getBoolean("whole-word", false);
		IWarlockStyle style = WarlockStylePreference.getStyle(node.node("style"));
		
		return new WarlockHighlight(text, literal, caseInsensitive, wholeWord, style);
	}
}
