package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.WarlockFont;
import cc.warlock.core.client.internal.WarlockStyle;

public class WarlockStyleProvider extends WarlockPreferenceProvider<IWarlockStyle> {
	private static final WarlockStyleProvider instance = new WarlockStyleProvider();
	
	protected WarlockStyleProvider() { }
	
	public static WarlockStyleProvider getInstance() {
		return instance;
	}
	
	protected String getNodeName() {
		return "style";
	}
	
	protected IWarlockStyle get(Preferences node) {
		WarlockStyle style = new WarlockStyle(node.get("name", null));
		style.setFullLine(node.getBoolean("full-line", false));
		WarlockColor fg = WarlockColorProvider.getInstance().get(node.node("color"));
		if(fg != null)
			style.setForegroundColor(fg);
		WarlockColor bg = WarlockColorProvider.getInstance().get(node.node("background-color"));
		if(bg != null)
			style.setBackgroundColor(bg);
		WarlockFont font = WarlockFontProvider.getInstance().get(node.node("font"));
		if(font != null)
			style.setFont(font);
		return style;
	}
	
	protected void set(Preferences node, IWarlockStyle style) {
		node.put("name", style.getName());
		node.putBoolean("full-line", style.isFullLine());
	}
}
