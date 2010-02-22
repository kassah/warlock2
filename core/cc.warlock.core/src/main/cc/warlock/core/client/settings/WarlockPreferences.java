package cc.warlock.core.client.settings;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.osgi.service.prefs.Preferences;

public class WarlockPreferences {
	private static WarlockPreferences instance = new WarlockPreferences();
	private static ConfigurationScope scope = new ConfigurationScope();
	private static IEclipsePreferences topLevel = scope.getNode("cc.warlock");
	
	protected WarlockPreferences() { }
	
	public static WarlockPreferences getInstance() {
		return instance;
	}
	
	public static ConfigurationScope getScope() {
		return scope;
	}
	
	public static Preferences getRootNode() {
		return topLevel;
	}
	
	public Preferences getNode() {
		return topLevel;
	}
	
	public void addNodeChangeListener(String path, INodeChangeListener listener) {
		try {
			getScope().getNode(path).addNodeChangeListener(listener);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addPreferenceChangeListener(String path, IPreferenceChangeListener listener) {
		try {
			getScope().getNode(path).addPreferenceChangeListener(listener);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
