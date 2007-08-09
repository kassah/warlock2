/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.script;

import java.io.Reader;

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
	 * @param commands This is a special interface that script engines can use to only deal with scripting issues
	 * @param scriptName The name of the script including the extension
	 * @param scriptReader A java.io.Reader that contains the content of this script
	 * @param argumetns The arguments passed to the script
	 * @return The script that is running.
	 */
	public IScript startScript(IScriptCommands commands, String scriptName, Reader scriptReader, String[] arguments);
	
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
