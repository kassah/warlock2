package cc.warlock.core.script.settings;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.settings.WarlockPreference;
import cc.warlock.core.client.settings.WarlockPreferences;

public class WarlockScriptPreference {
	public static WarlockPreference<Collection<String>> getDirectories() {
		ArrayList<String> results = new ArrayList<String>();

		Preferences node = WarlockPreferences.getRootNode().node("script").node("directory");
		try {
			for (String name : node.childrenNames()) {
				results.add(node.node(name).get("directory", null));
			}
		} catch(BackingStoreException e) {
			e.printStackTrace();
		}

		return new WarlockPreference<Collection<String>>(node.absolutePath(), results);
	}
}
