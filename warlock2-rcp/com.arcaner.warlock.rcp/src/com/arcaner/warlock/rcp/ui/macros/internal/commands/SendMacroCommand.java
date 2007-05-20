/*
 * Created on Mar 27, 2005
 */
package com.arcaner.warlock.rcp.ui.macros.internal.commands;

import com.arcaner.warlock.client.IWarlockClientViewer;
import com.arcaner.warlock.rcp.ui.macros.IMacroCommand;

/**
 * @author Marshall
 */
public class SendMacroCommand implements IMacroCommand {

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