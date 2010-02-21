package cc.warlock.core.script.settings;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.WarlockPreferenceArrayProvider;

public class WarlockScriptPreference extends WarlockPreferenceArrayProvider<String> {
	
	private static final WarlockScriptPreference instance = new WarlockScriptPreference();
	
	protected WarlockScriptPreference() { }
	
	public static WarlockScriptPreference getInstance() {
		return instance;
	}
	
	protected String getNodeName() {
		return "script/directory";
	}
	
	protected String get(Preferences node) {
		return node.get("path", null);
	}
	
	protected void set(Preferences node, String path) {
		node.put("path", path);
	}
}
