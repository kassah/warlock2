/*
 * Created on Mar 27, 2005
 */
package cc.warlock.rcp.ui.macros.internal.variables;

import cc.warlock.client.ICommand;
import cc.warlock.client.IWarlockClient;
import cc.warlock.client.IWarlockClientViewer;
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
		ICommand currentCommand = context.getCurrentCommand();
		if (currentCommand != null && !currentCommand.isInHistory())
		{
			client.getCommandHistory().addCommand(currentCommand);
		}
		
		ICommand command = client.getCommandHistory().prev();
			
		if(command != null) {
			return command.getCommand();
		}
		
		return null;
	}

}
