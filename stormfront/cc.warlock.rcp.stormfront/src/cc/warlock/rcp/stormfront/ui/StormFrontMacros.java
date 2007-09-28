package cc.warlock.rcp.stormfront.ui;

import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.swt.SWT;

import cc.warlock.core.stormfront.serversettings.server.MacroKey;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;
import cc.warlock.rcp.stormfront.ui.macros.CompassMacroHandler;
import cc.warlock.rcp.ui.macros.MacroRegistry;

public class StormFrontMacros {
	
	public static Hashtable<String, Integer> keys = new Hashtable<String, Integer>();
	public static Hashtable<String, Integer> mods = new Hashtable<String, Integer>();
	static {
		keys.put("Keypad 0", SWT.KEYPAD_0);
		keys.put("Keypad 1", SWT.KEYPAD_1);
		keys.put("Keypad 2", SWT.KEYPAD_2);
		keys.put("Keypad 3", SWT.KEYPAD_3);
		keys.put("Keypad 4", SWT.KEYPAD_4);
		keys.put("Keypad 5", SWT.KEYPAD_5);
		keys.put("Keypad 6", SWT.KEYPAD_6);
		keys.put("Keypad 7", SWT.KEYPAD_7);
		keys.put("Keypad 8", SWT.KEYPAD_8);
		keys.put("Keypad 9", SWT.KEYPAD_9);
		keys.put("Keypad +", SWT.KEYPAD_ADD);
		keys.put("Keypad /", SWT.KEYPAD_DIVIDE);
		keys.put("Keypad *", SWT.KEYPAD_MULTIPLY);
		keys.put("Keypad -", SWT.KEYPAD_SUBTRACT);
		keys.put("Keypad .", SWT.KEYPAD_DECIMAL);
		keys.put("Keypad Enter", SWT.KEYPAD_CR);
		keys.put("Enter", (int)SWT.CR);
		
		keys.put("F1", SWT.F1);
		keys.put("F2", SWT.F2);
		keys.put("F3", SWT.F3);
		keys.put("F4", SWT.F4);
		keys.put("F5", SWT.F5);
		keys.put("F6", SWT.F6);
		keys.put("F7", SWT.F7);
		keys.put("F8", SWT.F8);
		keys.put("F9", SWT.F9);
		keys.put("F10", SWT.F10);
		keys.put("F11", SWT.F11);
		keys.put("F12", SWT.F12);
		
		keys.put("Page Up", SWT.PAGE_UP);
		keys.put("Page Down", SWT.PAGE_DOWN);
		keys.put("UP", SWT.ARROW_UP);
		keys.put("DOWN", SWT.ARROW_DOWN);
		keys.put("Tab", (int)SWT.TAB);
		keys.put("Home", SWT.HOME);
		keys.put("End", SWT.END);
		keys.put("Esc", (int) SWT.ESC);
		
		mods.put("Ctrl", SWT.CTRL);
		mods.put("Alt", SWT.ALT);
		mods.put("Shift", SWT.SHIFT);
	}
	
	
	public static int modifierListToInt (ArrayList<String> modifiers)
	{
		int intMods = 0;
		
		for (String mod : modifiers)
		{
			if (mods.containsKey(mod))
			{
				intMods = intMods | mods.get(mod);
			}
		}
		
		return intMods;
	}
	
	public static void addMacrosFromServerSettings (ServerSettings settings)
	{
		CompassMacroHandler.instance();
		
		for (MacroKey key : settings.getMacroSet(0))
		{
			if (keys.containsKey(key.getKey()))
			{
				int keyCode = keys.get(key.getKey());
				int mods = modifierListToInt(key.getModifiers());
				
				MacroRegistry.instance().addMacro(MacroRegistry.createCommandMacro(keyCode, mods, key.getAction()));
			}
			else {
				if (key.getKey().length() == 1)
				{
					char k = key.getKey().charAt(0);
					
					if ((k >= 'A' && k <= 'Z') || (k >= '0' && k <= '9'))
					{
						int mods = modifierListToInt(key.getModifiers());
						MacroRegistry.instance().addMacro(MacroRegistry.createCommandMacro(k, mods, key.getAction()));
					}
				}
			}
		}
	}
}