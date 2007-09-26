package cc.warlock.core.script;

import java.io.Reader;
import java.util.ArrayList;

public abstract class AbstractScript implements IScript {

	protected ArrayList<IScriptListener> listeners;
	protected boolean suspended;
	protected Reader reader;
	protected IScriptInfo info;
	
	public AbstractScript (IScriptInfo info)
	{
		this.listeners = new ArrayList<IScriptListener>();
		
		this.info = info;
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
	
	public String getName() {
		
		return info.getScriptName();
	}
	
	public IScriptInfo getScriptInfo() {
		return info;
	}
}
