package cc.warlock.core.client.settings;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.osgi.service.prefs.Preferences;

public class WarlockClientPreferences extends WarlockPreferences {
	protected Preferences clientPrefs;
	
	public WarlockClientPreferences(String clientID) {
		clientPrefs = super.getNode().node("client/" + clientID);
	}
	
	public Preferences getNode() {
		return clientPrefs;
	}
	
	public void addNodeChangeListener(INodeChangeListener listener) {
		WarlockPreferences.getInstance().addNodeChangeListener(clientPrefs.absolutePath(), listener);
	}
	
	public void addPreferenceChangeListener(IPreferenceChangeListener listener) {
		WarlockPreferences.getInstance().addPreferenceChangeListener(clientPrefs.absolutePath(), listener);
	}
}
