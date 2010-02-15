package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.WarlockColor;

public class WarlockColorProvider implements WarlockPreferenceProvider<WarlockColor> {
	private static WarlockColorProvider instance;
	
	private WarlockColorProvider() { }
	
	protected static synchronized WarlockColorProvider getInstance() {
		if(instance == null)
			instance = new WarlockColorProvider();
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
