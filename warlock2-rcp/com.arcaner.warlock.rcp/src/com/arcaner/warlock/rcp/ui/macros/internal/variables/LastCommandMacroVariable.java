/*
 * Created on Mar 27, 2005
 */
package com.arcaner.warlock.rcp.ui.macros.internal.variables;

import com.arcaner.warlock.rcp.rcp.client.ICommand;
import com.arcaner.warlock.rcp.rcp.client.IWarlockClient;
import com.arcaner.warlock.rcp.rcp.client.IWarlockClientViewer;
import com.arcaner.warlock.rcp.ui.macros.IMacroVariable;

/**
 * @author Marshall
 */
public class LastCommandMacroVariable implements IMacroVariable {

	public String getIdentifier() {
		return "$lastCommand";
	}

	public String getValue(IWarlockClientViewer context) {
		
		IWarlockClient client = context.getWarlockClient();
		ICommand command = client.getCommandHistory().prev();
		
		if(command != null) {
			return command.getCommand();
		}
		
		return null;
	}

}
