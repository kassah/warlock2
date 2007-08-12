package cc.warlock.rcp.ui.macros;

import cc.warlock.client.IWarlockClientViewer;

/**
 * @author Marshall
 */
public interface IMacroHandler {

	/**
	 * Handle a macro.
	 * @param macro The macro to handle
	 * @param viewer The warlock client viewer that processed this macro.
	 * @return whether or not this should be the last handler for this macro.
	 */
	public boolean handleMacro (IMacro macro, IWarlockClientViewer viewer);
}
