package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.WarlockColor;

public class WarlockColorProvider implements WarlockPreferenceProvider<WarlockColor> {
	private static final WarlockColorProvider instance = new WarlockColorProvider();
	
	private WarlockColorProvider() { }
	
	public static WarlockColorProvider getInstance() {
		return instance;
	}
	
	public static WarlockColor getColor(Preferences node) {
		return new WarlockColor(node.get("value", null));
	}
	
	public static WarlockPreference<WarlockColor> get(Preferences node) {
		return new WarlockPreference<WarlockColor>(getInstance(),
				node.absolutePath(), getColor(node));
	}
	
	protected static void saveColor(Preferences node, WarlockColor value) {
		node.put("value", value.toString());
	}
	
	public void save(String path, WarlockColor value) {
		saveColor(WarlockPreferences.getScope().getNode(path), value);
	}
}
