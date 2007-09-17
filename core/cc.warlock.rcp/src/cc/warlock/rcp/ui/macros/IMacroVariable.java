/*
 * Created on Mar 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.ui.macros;

import cc.warlock.core.client.IWarlockClientViewer;

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
