package cc.warlock.core.client.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockHighlight;

public class WarlockHighlightProvider implements WarlockPreferenceProvider<IWarlockHighlight> {
	private static final String NODE_NAME = "highlight";
	
	private static final WarlockHighlightProvider instance = new WarlockHighlightProvider();
	
	private WarlockHighlightProvider() { }
	
	private static WarlockHighlightProvider getInstance() {
		return instance;
	}
	
	public static Collection<WarlockPreference<IWarlockHighlight>> getAll(WarlockClientPreferences prefs) {
		ArrayList<WarlockPreference<IWarlockHighlight>> results =
			new ArrayList<WarlockPreference<IWarlockHighlight>>();

		Preferences hnode = prefs.getNode().node(NODE_NAME);
		try {
			for (String name : hnode.childrenNames()) {
				Preferences node = hnode.node(name);
				results.add(new WarlockPreference<IWarlockHighlight>(getInstance(),
						node.absolutePath(), getHighlight(node)));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return results;
	}
	
	public static Map<String, WarlockPreference<IWarlockHighlight>> getMap(WarlockClientPreferences prefs) {
		HashMap<String, WarlockPreference<IWarlockHighlight>> results =
			new HashMap<String, WarlockPreference<IWarlockHighlight>>();

		Preferences hnode = prefs.getNode().node(NODE_NAME);
		try {
			for (String name : hnode.childrenNames()) {
				Preferences node = hnode.node(name);
				results.put(name, new WarlockPreference<IWarlockHighlight>(getInstance(),
						node.absolutePath(), getHighlight(node)));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return results;
	}
	
	protected static IWarlockHighlight getHighlight(Preferences node) {
		String text = node.get("text", null);
		boolean literal = node.getBoolean("literal", true);
		boolean caseInsensitive = node.getBoolean("case-insensitive", false);
		boolean wholeWord = node.getBoolean("whole-word", false);
		IWarlockStyle style = WarlockStyleProvider.getStyle(node.node("style"));
		
		return new WarlockHighlight(text, literal, caseInsensitive, wholeWord, style);
	}
	
	public static String addHighlight(WarlockClientPreferences prefs, IWarlockHighlight highlight) {
		Preferences hnode = prefs.getNode().node(NODE_NAME);
		
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
	
	public static void putHighlight(WarlockClientPreferences prefs, String id, IWarlockHighlight highlight) {
		saveHighlight(prefs.getNode().node(NODE_NAME).node(id), highlight);
	}
	
	protected static void saveHighlight(Preferences node, IWarlockHighlight highlight) {
		node.put("text", highlight.getText());
		node.putBoolean("literal", highlight.isLiteral());
		node.putBoolean("case-insensitive", highlight.isCaseInsensitive());
		node.putBoolean("whole-word", highlight.isWholeWord());
		WarlockStyleProvider.saveStyle(node.node("style"), highlight.getStyle());
	}
	
	public static void removeHighlight(WarlockClientPreferences prefs, String id) {
		try {
			prefs.getNode().node(NODE_NAME).node(id).removeNode();
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public void save(String path, IWarlockHighlight value) {
		saveHighlight(WarlockPreferences.getScope().getNode(path), value);
	}
	
	public static void addNodeChangeListener(WarlockClientPreferences prefs,
			INodeChangeListener listener) {
		String path = prefs.getNode().node(NODE_NAME).absolutePath();
		WarlockPreferences.getScope().getNode(path).addNodeChangeListener(listener);
	}
	
	public static void addPreferenceChangeListener(WarlockClientPreferences prefs,
			IPreferenceChangeListener listener) {
		String path = prefs.getNode().node(NODE_NAME).absolutePath();
		WarlockPreferences.getScope().getNode(path).addPreferenceChangeListener(listener);
	}
	
	public static void removeNodeChangeListener(WarlockClientPreferences prefs,
			INodeChangeListener listener) {
		String path = prefs.getNode().node(NODE_NAME).absolutePath();
		WarlockPreferences.getScope().getNode(path).removeNodeChangeListener(listener);
	}
	
	public static void removePreferenceChangeListener(WarlockClientPreferences prefs,
			IPreferenceChangeListener listener) {
		String path = prefs.getNode().node(NODE_NAME).absolutePath();
		WarlockPreferences.getScope().getNode(path).removePreferenceChangeListener(listener);
	}
}
