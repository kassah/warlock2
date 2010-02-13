package cc.warlock.core.client.settings.internal;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.internal.WarlockPreference;

public class WarlockVariablePreference {
	public static WarlockPreference<String> get(WarlockClientPreferences prefs, String name) {
		Preferences node = prefs.getClientPreferences().node("variable");
		return new WarlockPreference<String>(prefs, node.absolutePath(), node.get(name, null));
	}
	
	public static void set(WarlockClientPreferences prefs, String name, String value) {
		try {
			prefs.getClientPreferences().node("variable").put(name, value);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void remove(WarlockClientPreferences prefs, String name) {
		try {
			prefs.getClientPreferences().node("variable").remove(name);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
