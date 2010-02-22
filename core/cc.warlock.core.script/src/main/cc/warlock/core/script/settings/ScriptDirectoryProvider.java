package cc.warlock.core.script.settings;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.WarlockPreferenceArrayProvider;

public class ScriptDirectoryProvider extends WarlockPreferenceArrayProvider<String> {
	
	private static final ScriptDirectoryProvider instance = new ScriptDirectoryProvider();
	
	protected ScriptDirectoryProvider() { }
	
	public static ScriptDirectoryProvider getInstance() {
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
