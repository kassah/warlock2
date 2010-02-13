package cc.warlock.core.client.settings.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	
	public static WarlockPreference<Map<String, IWarlockHighlight>> getMap(WarlockClientPreferences prefs) {
		HashMap<String, IWarlockHighlight> results = new HashMap<String, IWarlockHighlight>();

		Preferences hnode = prefs.getClientPreferences().node("highlight");
		try {
			for (String name : hnode.childrenNames()) {
				results.put(name, getHighlight(hnode.node(name)));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return new WarlockPreference<Map<String, IWarlockHighlight>>(prefs, hnode.absolutePath(), results);
	}
	
	protected static IWarlockHighlight getHighlight(Preferences node) {
		String text = node.get("text", null);
		boolean literal = node.getBoolean("literal", true);
		boolean caseInsensitive = node.getBoolean("case-insensitive", false);
		boolean wholeWord = node.getBoolean("whole-word", false);
		IWarlockStyle style = WarlockStylePreference.getStyle(node.node("style"));
		
		return new WarlockHighlight(text, literal, caseInsensitive, wholeWord, style);
	}
	
	public static String addHighlight(WarlockClientPreferences prefs, WarlockHighlight highlight) {
		Preferences hnode = prefs.getClientPreferences().node("highlight");
		
		int max = 0;
		try {
			for (String name : hnode.childrenNames()) {
				int cur = Integer.parseInt(name);
				if(cur > max)
					max = cur;
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
		String name = Integer.toString(max + 1);
		saveHighlight(hnode.node(name), highlight);
		return name;
	}
	
	public static void putHighlight(WarlockClientPreferences prefs, String id, WarlockHighlight highlight) {
		saveHighlight(prefs.getClientPreferences().node("highlight").node(id), highlight);
	}
	
	protected static void saveHighlight(Preferences node, WarlockHighlight highlight) {
		node.put("text", highlight.getText());
		node.putBoolean("literal", highlight.isLiteral());
		node.putBoolean("case-insensitive", highlight.isCaseInsensitive());
		node.putBoolean("whole-word", highlight.isWholeWord());
		WarlockStylePreference.saveStyle(node.node("style"), highlight.getStyle());
	}
	
	public static void removeHighlight(WarlockClientPreferences prefs, String id) {
		try {
			prefs.getClientPreferences().node("highlight").node(id).removeNode();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
