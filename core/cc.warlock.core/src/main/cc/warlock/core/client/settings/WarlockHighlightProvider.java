package cc.warlock.core.client.settings;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockHighlight;

public class WarlockHighlightProvider extends WarlockArrayProvider<IWarlockHighlight> {
	
	private static final WarlockHighlightProvider instance = new WarlockHighlightProvider();
	
	private WarlockHighlightProvider() { }
	
	public static WarlockHighlightProvider getInstance() {
		return instance;
	}
	
	protected String getNodeName() {
		return "highlight";
	}
	
	protected IWarlockHighlight get(Preferences node) {
		String text = node.get("text", null);
		boolean literal = node.getBoolean("literal", true);
		boolean caseInsensitive = node.getBoolean("case-insensitive", false);
		boolean wholeWord = node.getBoolean("whole-word", false);
		IWarlockStyle style = WarlockStyleProvider.getStyle(node.node("style"));
		
		return new WarlockHighlight(text, literal, caseInsensitive, wholeWord, style);
	}
	
	public static void putHighlight(WarlockClientPreferences prefs, String id, IWarlockHighlight highlight) {
		saveHighlight(prefs.getNode().node(NODE_NAME).node(id), highlight);
	}
	
	protected void save(Preferences node, IWarlockHighlight highlight) {
		node.put("text", highlight.getText());
		node.putBoolean("literal", highlight.isLiteral());
		node.putBoolean("case-insensitive", highlight.isCaseInsensitive());
		node.putBoolean("whole-word", highlight.isWholeWord());
		WarlockStyleProvider.saveStyle(node.node("style"), highlight.getStyle());
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
