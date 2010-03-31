package cc.warlock.rcp.stormfront.ui.prefs;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.WarlockPreferences;
import cc.warlock.rcp.stormfront.StormFrontRCPPlugin;

public class WarlockDefaultPreferences extends WarlockPreferences {
	
	private static final WarlockDefaultPreferences instance =
		new WarlockDefaultPreferences();
	private DefaultScope scope = new DefaultScope();
	private IEclipsePreferences topLevel =
		scope.getNode(StormFrontRCPPlugin.PLUGIN_ID);
	
	protected WarlockDefaultPreferences() { }
	
	public static WarlockDefaultPreferences getInstance() {
		return instance;
	}
	
	public IScopeContext getScope() {
		return scope;
	}
	
	public Preferences getNode() {
		return topLevel;
	}
}
