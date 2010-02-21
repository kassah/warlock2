package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;


public class WarlockVariableProvider extends WarlockPreferenceProvider<String> {
	private static WarlockVariableProvider instance = new WarlockVariableProvider();
	
	private WarlockVariableProvider() { }
	
	public static WarlockVariableProvider getInstance() {
		return instance;
	}
	
	protected String getNodeName() {
		return "variable";
	}
	
	protected String get(Preferences node) {
		return node.get("value", null);
	}
	
	protected void set(Preferences node, String value) {
		try {
			node.put("value", value);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
