package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.WarlockColor;

/*
 * This class is meant for internal use only.
 */
public class WarlockColorProvider extends WarlockPreferenceProvider<WarlockColor> {
	private static final WarlockColorProvider instance = new WarlockColorProvider();
	
	private WarlockColorProvider() { }
	
	public static WarlockColorProvider getInstance() {
		return instance;
	}
	
	protected String getNodeName() {
		return null;
	}

	public WarlockColor get(Preferences node) {
		String val = node.get("value", null);
		if (val == null)
			return new WarlockColor(); // Blank
		else
			return new WarlockColor(node.get("value", null));
	}
	
	protected void set(Preferences node, WarlockColor value) {
		node.put("value", value.toString());
	}
}
