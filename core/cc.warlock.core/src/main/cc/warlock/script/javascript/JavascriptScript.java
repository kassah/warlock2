/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.script.javascript;

import cc.warlock.script.AbstractScript;
import cc.warlock.script.IScriptCommands;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JavascriptScript extends AbstractScript {

	private String name;
	
	public JavascriptScript (IScriptCommands commands, String name)
	{
		super(commands);
		
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public boolean isRunning() {
		return true;
	}

	public void stop() {

	}
	public void suspend() {

	}
	
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
