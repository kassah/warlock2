package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.WarlockFont;
import cc.warlock.core.client.internal.WarlockStyle;

public class WarlockStyleProvider implements WarlockPreferenceProvider<IWarlockStyle> {
	private static WarlockStyleProvider instance;
	
	private WarlockStyleProvider() { }
	
	protected static synchronized WarlockStyleProvider getInstance() {
		if(instance == null)
			instance = new WarlockStyleProvider();
		return instance;
	}
	
	protected static IWarlockStyle getStyle(Preferences node) {
		WarlockStyle style = new WarlockStyle(node.get("name", null));
		style.setFullLine(node.getBoolean("full-line", false));
		WarlockColor fg = WarlockColorProvider.getColor(node.node("color"));
		if(fg != null)
			style.setForegroundColor(fg);
		WarlockColor bg = WarlockColorProvider.getColor(node.node("background-color"));
		if(bg != null)
			style.setBackgroundColor(bg);
		WarlockFont font = WarlockFontProvider.getFont(node.node("font"));
		if(font != null)
			style.setFont(font);
		return style;
	}
	
	public static WarlockPreference<IWarlockStyle> get(WarlockClientPreferences prefs, String name) {
		Preferences node = prefs.getNode().node("style").node(name);
		return new WarlockPreference<IWarlockStyle>(getInstance(),
				node.absolutePath(), getStyle(node));
	}
	
	protected static void saveStyle(Preferences node, IWarlockStyle style) {
		node.put("name", style.getName());
		node.putBoolean("full-line", style.isFullLine());
	}
	
	public void save(String path, IWarlockStyle style) {
		saveStyle(WarlockPreferences.getScope().getNode(path), style);
	}
}
