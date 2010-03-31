package cc.warlock.core.client.settings;

public class WarlockDefaultPreferencesRegistry {
	private static final WarlockDefaultPreferencesRegistry instance =
		new WarlockDefaultPreferencesRegistry();
	private WarlockPreferences defaultPrefs;
	
	protected WarlockDefaultPreferencesRegistry() { }
	
	public static WarlockDefaultPreferencesRegistry getInstance() {
		return instance;
	}
	
	public void set(WarlockPreferences prefs) {
		defaultPrefs = prefs;
	}
	
	public WarlockPreferences get() {
		return defaultPrefs;
	}
}
