package cc.warlock.script;

import java.util.ArrayList;

public abstract class AbstractScript implements IScript {

	protected IScriptCommands commands;
	protected ArrayList<IScriptListener> listeners;
	protected boolean suspended;
	
	public AbstractScript (IScriptCommands commands)
	{
		this.commands = commands;
		this.listeners = new ArrayList<IScriptListener>();
	}
	
	public void resume() {
		this.suspended = false;
		
		for (IScriptListener listener : listeners) listener.scriptResumed(this);
	}

	public void stop() {
		for (IScriptListener listener : listeners) listener.scriptStopped(this, true);
	}

	public void suspend() {
		this.suspended = true;
		
		for (IScriptListener listener : listeners) listener.scriptPaused(this);
	}
	
	public boolean isSuspended() {
		return suspended;
	}
	
	public void addScriptListener(IScriptListener listener) {
		listeners.add(listener);
	}
	
	public void removeScriptListener(IScriptListener listener) {
		if (listeners.contains(listener))
			listeners.remove(listener);
	}

	public IScriptCommands getScriptCommands() {
		return commands;
	}
}
