/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.script;


/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IScript {

	public String getName ();
	
	public boolean isRunning ();
	public boolean isSuspended();
	
	public void stop();
	
	public void suspend();
	
	public void resume();
	
	public void movedToRoom();
	
	public void addScriptListener (IScriptListener listener);
	
	public void removeScriptListener (IScriptListener listener);
	
	public IScriptEngine getScriptEngine();
	
	public IScriptInfo getScriptInfo();
	
}
