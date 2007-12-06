/*
 * Created on Mar 27, 2005
 */
package cc.warlock.rcp.ui.macros.internal;

import java.util.ArrayList;

import org.eclipse.swt.SWT;

import cc.warlock.rcp.ui.macros.IMacro;
import cc.warlock.rcp.ui.macros.MacroRegistry;

/**
 * @author Marshall
 * 
 * Loads the default macros from the sytem .macros file
 */
public class SystemMacros {
	public static IMacro[] getSystemMacros ()
	{
		ArrayList<IMacro> systemMacros = new ArrayList<IMacro>();
		
		ScriptMacroHandler scriptMacroHandler = new ScriptMacroHandler();
		for (IMacro macro : scriptMacroHandler.getMacros())
		{
			systemMacros.add(macro);
		}
		
		CommandHistoryMacroHandler commandHistoryMacroHandler = new CommandHistoryMacroHandler();
		for (IMacro macro : commandHistoryMacroHandler.getMacros())
		{
			systemMacros.add(macro);
		}
		
//		systemMacros.add(MacroRegistry.createCommandMacro(SWT.KEYPAD_8, "north\\r"));
//		systemMacros.add(MacroRegistry.createCommandMacro(SWT.KEYPAD_9, "northeast\\r"));
//		systemMacros.add(MacroRegistry.createCommandMacro(SWT.KEYPAD_6, "east\\r"));
//		systemMacros.add(MacroRegistry.createCommandMacro(SWT.KEYPAD_3, "southeast\\r"));
//		systemMacros.add(MacroRegistry.createCommandMacro(SWT.KEYPAD_2, "south\\r"));
//		systemMacros.add(MacroRegistry.createCommandMacro(SWT.KEYPAD_1, "southwest\\r"));
//		systemMacros.add(MacroRegistry.createCommandMacro(SWT.KEYPAD_4, "west\\r"));
//		systemMacros.add(MacroRegistry.createCommandMacro(SWT.KEYPAD_7, "northwest\\r"));
//		systemMacros.add(MacroRegistry.createCommandMacro(SWT.KEYPAD_DECIMAL, "up\\r"));
//		systemMacros.add(MacroRegistry.createCommandMacro(SWT.KEYPAD_0, "down\\r"));
//		systemMacros.add(MacroRegistry.createCommandMacro(SWT.KEYPAD_5, "out\\r"));
		systemMacros.add(MacroRegistry.createCommandMacro(SWT.KEYPAD_CR, "$lastCommand\\r"));
		
		return systemMacros.toArray(new IMacro[systemMacros.size()]);
	}
}
