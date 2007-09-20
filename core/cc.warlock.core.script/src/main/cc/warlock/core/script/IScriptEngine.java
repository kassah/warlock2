/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.script;

import java.util.List;

import cc.warlock.core.client.IWarlockClient;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IScriptEngine {

	/**
	 * Create a script (but do not start it).
	 * 
	 * @param info The information needed to create this script
	 * @return A script object
	 */
	public IScript createScript (IScriptInfo info);
	
	/**
	 * This method will run the script at the given path. Note that this should return immediately, so the engine
	 * will need to probably create a thread to execute the script.
	 * 
	 * @param script the script to start
	 * @param client the client to start the script on
	 * @param argumetns The arguments passed to the script
	 */
	public void startScript(IScript script, IWarlockClient client, String[] arguments);
	
	public boolean supports (IScriptInfo scriptInfo);
	
	/**
	 * @return The unique id of this scripting engine.
	 */
	public String getScriptEngineId();
	
	/**
	 * @return A display name for this scripting engine.
	 */
	public String getScriptEngineName();
	
	/**
	 * @return a list of currently running scripts for this script engine.
	 */
	public List<IScript> getRunningScripts();
	
}
