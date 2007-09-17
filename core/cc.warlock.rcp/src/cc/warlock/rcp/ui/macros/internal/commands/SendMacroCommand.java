/*
 * Created on Mar 27, 2005
 */
package cc.warlock.rcp.ui.macros.internal.commands;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacroCommand;


/**
 * @author Marshall
 */
public class SendMacroCommand implements IMacroCommand {

	public int getPrecedence() {
		return 1002;
	}
	
	public String getIdentifier() {
		return "\\r";
	}

	public String execute(IWarlockClientViewer context, String currentCommand, int position) {
		String toSend = currentCommand.substring(0, position);
		
		context.getWarlockClient().send(toSend);
		context.getWarlockClient().getDefaultStream().echo(toSend);
		
		return currentCommand.substring(position + 2);
	}

}