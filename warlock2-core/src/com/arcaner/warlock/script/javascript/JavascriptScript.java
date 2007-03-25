/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.script.javascript;

import org.eclipse.core.runtime.IPath;

import com.arcaner.warlock.script.IScript;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JavascriptScript implements IScript {

	private IPath path;
	
	public JavascriptScript (IPath path)
	{
		this.path = path;
	}
	
	public IPath getPath() {
		return path;
	}

	public boolean isRunning() {
		return true;
	}

	public void stop() {

	}
	public void suspend() {

	}

}
