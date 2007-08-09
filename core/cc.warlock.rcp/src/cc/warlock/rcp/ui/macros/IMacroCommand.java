/*
 * Created on Mar 27, 2005
 */
package cc.warlock.rcp.ui.macros;

import cc.warlock.client.IWarlockClientViewer;

/**
 * @author Marshall
 */
public interface IMacroCommand {

	public String getIdentifier ();
	public String execute (IWarlockClientViewer context, String currentCommand, int position);
}
