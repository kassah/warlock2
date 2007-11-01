/*
 * Created on Mar 27, 2005
 */
package cc.warlock.rcp.ui.macros.internal.variables;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacroVariable;


/**
 * @author Marshall
 */
public class LastCommandMacroVariable implements IMacroVariable {

	public String getIdentifier() {
		return "$lastCommand";
	}

	public String getValue(IWarlockClientViewer context) {
		
		IWarlockClient client = context.getWarlockClient();
		
		ICommand command = client.getCommandHistory().getLastCommand();
			
		if(command != null) {
			return command.getCommand();
		}
		
		return null;
	}

}
