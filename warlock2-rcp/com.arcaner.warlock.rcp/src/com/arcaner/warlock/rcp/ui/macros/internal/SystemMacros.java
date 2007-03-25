/*
 * Created on Mar 27, 2005
 */
package com.arcaner.warlock.rcp.ui.macros.internal;

import org.eclipse.swt.SWT;

import com.arcaner.warlock.rcp.ui.macros.IMacro;

/**
 * @author Marshall
 * 
 * Loads the default macros from the sytem .macros file
 */
public class SystemMacros {

	public static IMacro[] getSystemMacros ()
	{
		return new IMacro[] {
			new Macro(SWT.CR, "\\r"),
			new Macro(SWT.ARROW_UP, "\\x$lastCommand"),
			new Macro(SWT.ARROW_DOWN, "\\x$nextCommand")
		};
	}

}
