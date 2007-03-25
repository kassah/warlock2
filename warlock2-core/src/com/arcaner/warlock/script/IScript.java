/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.script;

import org.eclipse.core.runtime.IPath;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IScript {

	public IPath getPath ();
	
	public boolean isRunning ();
	
	public void stop();
	
	public void suspend();
	
}
