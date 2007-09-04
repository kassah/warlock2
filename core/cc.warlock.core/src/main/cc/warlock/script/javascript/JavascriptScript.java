/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.script.javascript;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import cc.warlock.script.AbstractScript;
import cc.warlock.script.IScriptCommands;
import cc.warlock.script.IScriptListener;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JavascriptScript extends AbstractScript {

	private String name;
	private JavascriptEngine engine;
	private boolean stopped;
	private Context context;
	
	public JavascriptScript (IScriptCommands commands, String name, JavascriptEngine engine)
	{
		super(commands);
		
		this.commands = new JavascriptCommands(commands, this);
		
		this.name = name;
		this.engine = engine;
	}
	
	public String getName() {
		return name;
	}

	public boolean isRunning() {
		return !stopped;
	}
	
	public void start () {
		stopped = false;
		
		for (IScriptListener listener : listeners) listener.scriptStarted(this);
	}

	public void stop() {
		stopped = true;
		commands.stop();
		
		super.stop();
	}
	
	public void suspend() {

	}
	
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	public JavascriptCommands getCommands() {
		return (JavascriptCommands) commands;
	}
	
	public Scriptable getScope() {
		return engine.scope;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
