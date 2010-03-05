package cc.warlock.core.stormfront.preferences;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.WarlockPreferences;

public class StormFrontPreferences extends WarlockPreferences {
	private static StormFrontPreferences instance = new StormFrontPreferences();
	private static IEclipsePreferences topLevel = scope.getNode("cc.warlock.stormfront");
	
	protected StormFrontPreferences() { }
	
	public static StormFrontPreferences getInstance() {
		return instance;
	}
	
	public static Preferences getRootNode() {
		return topLevel;
	}
	
	@Override
	public Preferences getNode() {
		return topLevel;
	}
}
