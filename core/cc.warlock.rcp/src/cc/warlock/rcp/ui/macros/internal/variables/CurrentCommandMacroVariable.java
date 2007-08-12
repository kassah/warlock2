/*
 * Created on Mar 27, 2005
 */
package cc.warlock.rcp.ui.macros.internal.variables;

import cc.warlock.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacroVariable;


/**
 * @author Marshall
 */
public class CurrentCommandMacroVariable implements IMacroVariable {

	public String getIdentifier() {
		return "$currentCommand";
	}
	
	public String getValue(IWarlockClientViewer context) {
		return context.getCurrentCommand().getCommand();
	}

}
