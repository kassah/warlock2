/*
 * Created on Mar 27, 2005
 */
package cc.warlock.rcp.ui.macros.internal;

import org.eclipse.swt.SWT;

import cc.warlock.rcp.ui.macros.IMacro;

/**
 * @author Marshall
 * 
 * Loads the default macros from the sytem .macros file
 */
public class SystemMacros {

	public static IMacro[] getSystemMacros ()
	{
		return new IMacro[] {
			new Macro(SWT.CR, "$currentCommand\\r"),
			new Macro(SWT.ARROW_UP, "$lastCommand"),
			new Macro(SWT.ARROW_DOWN, "$nextCommand"),
			new Macro(SWT.KEYPAD_8, "north\\r"),
			new Macro(SWT.KEYPAD_9, "northeast\\r"),
			new Macro(SWT.KEYPAD_6, "east\\r"),
			new Macro(SWT.KEYPAD_3, "southeast\\r"),
			new Macro(SWT.KEYPAD_2, "south\\r"),
			new Macro(SWT.KEYPAD_1, "southwest\\r"),
			new Macro(SWT.KEYPAD_4, "west\\r"),
			new Macro(SWT.KEYPAD_7, "northwest\\r"),
			new Macro(SWT.KEYPAD_DECIMAL, "up\\r"),
			new Macro(SWT.KEYPAD_0, "down\\r"),
			new Macro(SWT.KEYPAD_5, "out\\r"),
			new Macro(SWT.KEYPAD_CR, "$lastCommand\\r")
		};
	}

}
