package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockHighlight;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockHighlight;

public class WarlockHighlightProvider extends WarlockPreferenceArrayProvider<IWarlockHighlight> {
	
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
		IWarlockStyle style = WarlockStyleProvider.getInstance().get(node.node("style"));
		
		return new WarlockHighlight(text, literal, caseInsensitive, wholeWord, style);
	}
	
	protected void set(Preferences node, IWarlockHighlight highlight) {
		node.put("text", highlight.getText());
		node.putBoolean("literal", highlight.isLiteral());
		node.putBoolean("case-insensitive", highlight.isCaseInsensitive());
		node.putBoolean("whole-word", highlight.isWholeWord());
		WarlockStyleProvider.getInstance().set(node.node("style"), highlight.getStyle());
	}
}
