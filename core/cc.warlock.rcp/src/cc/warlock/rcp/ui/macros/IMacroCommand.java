/*
 * Created on Mar 27, 2005
 */
package cc.warlock.rcp.ui.macros;

import cc.warlock.core.client.IWarlockClientViewer;

/**
 * @author Marshall
 */
public interface IMacroCommand {

	public String getIdentifier ();
	public void execute (IWarlockClientViewer context);

}
