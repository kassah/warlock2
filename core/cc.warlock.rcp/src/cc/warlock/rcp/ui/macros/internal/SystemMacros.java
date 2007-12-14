/*
 * Created on Mar 27, 2005
 */
package cc.warlock.rcp.ui.macros.internal;

import java.util.ArrayList;

import cc.warlock.rcp.ui.macros.IMacro;

/**
 * @author Marshall
 * 
 * Loads the default macros from the sytem .macros file
 */
public class SystemMacros {
	public static IMacro[] getSystemMacros ()
	{
		ArrayList<IMacro> systemMacros = new ArrayList<IMacro>();
		
		SystemMacroHandler commandHistoryMacroHandler = new SystemMacroHandler();
		for (IMacro macro : commandHistoryMacroHandler.getMacros())
		{
			systemMacros.add(macro);
		}
		
		return systemMacros.toArray(new IMacro[systemMacros.size()]);
	}
}
