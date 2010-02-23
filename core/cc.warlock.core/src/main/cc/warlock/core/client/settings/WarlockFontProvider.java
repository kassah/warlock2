package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.WarlockFont;

/*
 * This class should not be used directly
 */
public class WarlockFontProvider extends WarlockPreferenceProvider<WarlockFont> {
	private static final WarlockFontProvider instance = new WarlockFontProvider();
	
	private WarlockFontProvider() { }
	
	public static WarlockFontProvider getInstance() {
		return instance;
	}
	
	protected String getNodeName() {
		return "font";
	}
	
	protected WarlockFont get(Preferences node) {
		WarlockFont font = new WarlockFont();
		String family = node.get("family", null);
		if(family != null)
			font.setFamilyName(family);
		int size = node.getInt("size", -1);
		if(size != -1)
			font.setSize(size);
		return font;
	}
	
	protected void set(Preferences node, WarlockFont value) {
		node.put("family", value.getFamilyName());
		node.putInt("size", value.getSize());
	}
}
