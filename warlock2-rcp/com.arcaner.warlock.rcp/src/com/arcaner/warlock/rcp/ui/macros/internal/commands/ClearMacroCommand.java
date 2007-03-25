/*
 * Created on Mar 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.rcp.ui.macros.internal.commands;

import com.arcaner.warlock.rcp.rcp.client.IWarlockClientViewer;
import com.arcaner.warlock.rcp.ui.macros.IMacroCommand;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClearMacroCommand implements IMacroCommand {

	public String getIdentifier() {
		return "\\x";
	}

	public String execute(IWarlockClientViewer context, String currentCommand, int position) {
		return currentCommand.substring(position + 2);		
	}

}
