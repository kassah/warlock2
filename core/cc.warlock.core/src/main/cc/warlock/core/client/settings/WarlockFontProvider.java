package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.WarlockFont;

public class WarlockFontProvider implements WarlockPreferenceProvider<WarlockFont> {
	private static WarlockFontProvider instance;
	
	private WarlockFontProvider() { }
	
	protected static synchronized WarlockFontProvider getInstance() {
		if(instance == null)
			instance = new WarlockFontProvider();
		return instance;
	}
	
	public static WarlockFont getFont(Preferences node) {
		WarlockFont font = new WarlockFont();
		String family = node.get("family", null);
		if(family != null)
			font.setFamilyName(family);
		int size = node.getInt("size", -1);
		if(size != -1)
			font.setSize(size);
		return font;
	}
	
	public static WarlockPreference<WarlockFont> get(Preferences node) {
		return new WarlockPreference<WarlockFont>(getInstance(),
				node.absolutePath(), getFont(node));
	}
	
	protected static void saveFont(Preferences node, WarlockFont value) {
		node.put("family", value.getFamilyName());
		node.putInt("size", value.getSize());
	}
	
	public void save(String path, WarlockFont value) {
		saveFont(WarlockPreferences.getScope().getNode(path), value);
	}
}
