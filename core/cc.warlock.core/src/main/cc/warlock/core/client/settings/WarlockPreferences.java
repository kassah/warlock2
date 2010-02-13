package cc.warlock.core.client.settings;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.osgi.service.prefs.Preferences;

public class WarlockPreferences {
	private static ConfigurationScope scope;
	private static IEclipsePreferences topLevel;
	
	public static synchronized ConfigurationScope getScope() {
		if(scope == null)
			scope = new ConfigurationScope();
		return scope;
	}
	
	public static synchronized Preferences getRootNode() {
		if(topLevel == null)
			topLevel = scope.getNode("cc.warlock");
		return topLevel;
	}
	
	public static void addNodeChangeListener(String path, INodeChangeListener listener) {
		try {
			getScope().getNode(path).addNodeChangeListener(listener);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addPreferenceChangeListener(String path, IPreferenceChangeListener listener) {
		try {
			getScope().getNode(path).addPreferenceChangeListener(listener);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
