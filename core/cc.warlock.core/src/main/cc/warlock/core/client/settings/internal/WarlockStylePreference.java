package cc.warlock.core.client.settings.internal;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockStyle;

public class WarlockStylePreference {
	protected static IWarlockStyle getStyle(Preferences node) {
		WarlockStyle style = new WarlockStyle(node.get("name", null));
		style.setFullLine(node.getBoolean("full-line", false));
		return style;
	}
	
	protected static void saveStyle(Preferences node, IWarlockStyle style) {
		node.put("name", style.getName());
		node.putBoolean("full-line", style.isFullLine());
	}
}
