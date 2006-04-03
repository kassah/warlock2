/*
 * Created on Mar 27, 2005
 */
package com.arcaner.warlock.ui.macros;

import com.arcaner.warlock.client.IWarlockClientViewer;

/**
 * @author Marshall
 */
public interface IMacroCommand {

	public String getIdentifier ();
	public String execute (IWarlockClientViewer context, String currentCommand, int position);
}
