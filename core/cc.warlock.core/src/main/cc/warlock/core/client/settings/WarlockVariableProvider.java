package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;


public class WarlockVariableProvider implements WarlockPreferenceProvider<String> {
	private static WarlockVariableProvider instance = new WarlockVariableProvider();
	
	private WarlockVariableProvider() { }
	
	public static WarlockVariableProvider getInstance() {
		return instance;
	}
	
	public static WarlockPreference<String> get(WarlockClientPreferences prefs, String name) {
		Preferences node = prefs.getNode().node("variable").node(name);
		return new WarlockPreference<String>(getInstance(),
				node.absolutePath(), node.get("value", null));
	}
	
	public static void set(WarlockClientPreferences prefs, String name, String value) {
		try {
			saveVariable(prefs.getNode().node("variable").node(name), value);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void remove(WarlockClientPreferences prefs, String name) {
		try {
			prefs.getNode().node("variable").node(name).removeNode();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static void saveVariable(Preferences node, String value) {
		node.put("value", value);
	}
	
	public void save(String path, String value) {
		saveVariable(WarlockPreferences.getScope().getNode(path), value);
	}
}
