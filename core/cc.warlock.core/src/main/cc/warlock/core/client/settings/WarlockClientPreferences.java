package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;

public class WarlockClientPreferences extends WarlockPreferences {
	protected Preferences clientPrefs;
	
	public WarlockClientPreferences(String clientID) {
		clientPrefs = getRootNode().node("client/" + clientID);
	}
	
	public Preferences getNode() {
		return clientPrefs;
	}
}
