/*
 * Created on Mar 27, 2005
 */
package com.arcaner.warlock.rcp.ui.macros.internal.variables;

import com.arcaner.warlock.rcp.rcp.client.IWarlockClientViewer;
import com.arcaner.warlock.rcp.ui.macros.IMacroVariable;

/**
 * @author Marshall
 */
public class CurrentCommandMacroVariable implements IMacroVariable {

	public String getIdentifier() {
		return "$currentCommand";
	}
	
	public String getValue(IWarlockClientViewer context) {
		return context.getCurrentCommand();
	}

}
