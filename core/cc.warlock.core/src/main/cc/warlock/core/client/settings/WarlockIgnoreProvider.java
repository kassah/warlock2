package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockPattern;
import cc.warlock.core.client.internal.WarlockPattern;

public class WarlockIgnoreProvider extends WarlockPreferenceProvider<IWarlockPattern> {
	private static final WarlockIgnoreProvider instance = new WarlockIgnoreProvider();
	
	private WarlockIgnoreProvider() { }
	
	public static WarlockIgnoreProvider getInstance() {
		return instance;
	}
	
	public String getNodeName() {
		return "ignore";
	}
	
	protected IWarlockPattern get(Preferences node) {
		String text = node.get("text", null);
		boolean literal = node.getBoolean("literal", true);
		boolean caseInsensitive = node.getBoolean("case-insensitive", false);
		boolean wholeWord = node.getBoolean("whole-word", false);
		
		return new WarlockPattern(text, literal, caseInsensitive, wholeWord);
	}
	
	protected void set(Preferences node, IWarlockPattern pattern) {
		node.put("text", pattern.getText());
		node.putBoolean("literal", pattern.isLiteral());
		node.putBoolean("case-insensitive", pattern.isCaseInsensitive());
		node.putBoolean("whole-word", pattern.isWholeWord());
	}
}
