/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.rcp.stormfront.settings;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.swt.SWT;

import cc.warlock.core.client.settings.macro.CommandMacroHandler;
import cc.warlock.core.client.settings.macro.internal.Macro;
import cc.warlock.core.stormfront.settings.StormFrontServerSettings;
import cc.warlock.core.stormfront.settings.internal.StormFrontClientSettings;
import cc.warlock.core.stormfront.settings.server.MacroKey;

/**
 * @author marshall
 *
 */
@SuppressWarnings("deprecation")
public class StormFrontSWTSettings extends StormFrontServerSettings {

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
	
	public static StormFrontServerSettings instance() {
		if (StormFrontServerSettings._instance == null || !(StormFrontServerSettings._instance instanceof StormFrontSWTSettings)) {
			StormFrontServerSettings._instance = new StormFrontSWTSettings();
		}
		return StormFrontServerSettings._instance;
	}
	
	public static void importSettings (InputStream stream, StormFrontClientSettings settings)
	{
		instance().importServerSettings(stream, settings);
	}
	
	@Override
	protected void importMacro(MacroKey macroKey, StormFrontClientSettings settings) {
		String keyString = macroKey.getKey();
		if (keys.containsKey(keyString))
		{
			int keyCode = keys.get(keyString);
			int mods = modifierListToInt(macroKey.getModifiers());
			
			
			Macro macro = new Macro(settings.getMacroConfigurationProvider(), keyCode, mods);
			macro.addHandler(new CommandMacroHandler(macroKey.getAction()));
			
			settings.getMacroConfigurationProvider().addMacro(macro);
		}
		else {
			if (keyString.length() == 1)
			{
				char k = macroKey.getKey().toLowerCase().charAt(0);
				
				if ((k >= 'a' && k <= 'z') || (k >= '0' && k <= '9'))
				{
					int mods = modifierListToInt(macroKey.getModifiers());
					
					Macro macro = new Macro(settings.getMacroConfigurationProvider(), k, mods);
					macro.addHandler(new CommandMacroHandler(macroKey.getAction()));
					
					settings.getMacroConfigurationProvider().addMacro(macro);
				}
			}
		}
	}
}
