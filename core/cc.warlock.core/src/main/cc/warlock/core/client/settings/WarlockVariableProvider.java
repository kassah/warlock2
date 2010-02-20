package cc.warlock.core.client.settings;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;


public class WarlockVariableProvider extends WarlockPreferenceProvider<String> {
	private static WarlockVariableProvider instance = new WarlockVariableProvider();
	
	private WarlockVariableProvider() { }
	
	public static WarlockVariableProvider getInstance() {
		return instance;
	}
	
	public String getNodeName() {
		return "variable";
	}
	
	public String get(Preferences node) {
		return node.get("value", null);
	}
	
	
	public static void set(WarlockClientPreferences prefs, String name, String value) {
		try {
			getInstance().saveNode(prefs.getNode().node("variable").node(name), value);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void saveNode(Preferences node, String value) {
		node.put("value", value);
	}
}
