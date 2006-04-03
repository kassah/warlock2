/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.script;

import org.eclipse.core.runtime.IPath;

import com.arcaner.warlock.client.IWarlockClient;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IScriptEngine {

	/**
	 * This method will run the script at the given path. Note that this should return immediately, so the engine
	 * will need to probably create a thread to execute the script.
	 * 
	 * @param client The warlock client associated with this script
	 * @return The script that is running.
	 */
	public IScript startScript(IWarlockClient client, IPath path);
	
	/**
	 * @return The file extensions that this scripting engine can handle.
	 */
	public String[] getSupportedExtensions();
	
	/**
	 * @return The unique id of this scripting engine.
	 */
	public String getScriptEngineId();
	
	/**
	 * @return A display name for this scripting engine.
	 */
	public String getScriptEngineName();
	
}
