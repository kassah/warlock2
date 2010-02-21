package cc.warlock.core.client.settings;

import org.osgi.service.prefs.Preferences;

import cc.warlock.core.client.internal.WarlockMacro;

public class WarlockMacroProvider extends WarlockPreferenceProvider<WarlockMacro> {
	private static final WarlockMacroProvider instance = new WarlockMacroProvider();
	
	private WarlockMacroProvider() { }
	
	public static WarlockMacroProvider getInstance() {
		return instance;
	}
	
	protected String getNodeName() {
		return "macro";
	}
	
	protected WarlockMacro get(Preferences node) {
		int keycode = node.getInt("keycode", 0);
		int modifiers = node.getInt("modifiers", 0);
		String command = node.get("command", null);
		
		return new WarlockMacro(keycode, modifiers, command);
	}
	
	protected void set(Preferences node, WarlockMacro macro) {
		node.putInt("keycode", macro.getKeycode());
		node.putInt("modifiers", macro.getModifiers());
		node.put("command", macro.getCommand());
	}
}
