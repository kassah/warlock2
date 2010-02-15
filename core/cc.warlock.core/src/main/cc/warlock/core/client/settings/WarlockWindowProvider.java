package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockStyle;

public class WarlockWindowProvider {
	public static WarlockPreference<IWarlockStyle> get(WarlockClientPreferences prefs, String name) {
		Preferences node = prefs.getNode().node("window").node(name);
		return new WarlockPreference<IWarlockStyle>(WarlockStyleProvider.getInstance(),
				node.absolutePath(), WarlockStyleProvider.getStyle(node));
	}
}
