/*
 * Created on Mar 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.rcp.ui.macros;

import com.arcaner.warlock.client.IWarlockClientViewer;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IMacroVariable {

	public String getIdentifier();
	
	public String getValue(IWarlockClientViewer context);
}
