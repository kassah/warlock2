package cc.warlock.script;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractScript implements IScript {

	protected IScriptCommands commands;
	protected ArrayList<IScriptListener> listeners;
	protected boolean suspended;
	protected Lock lock = new ReentrantLock();
	protected Condition suspend = lock.newCondition();
	
	public AbstractScript (IScriptCommands commands)
	{
		this.commands = commands;
		this.listeners = new ArrayList<IScriptListener>();
	}
	
	public void resume() {
		lock.lock();
		try {
			if(suspended) {
				suspended = false;
		
				suspend.signalAll();
				for (IScriptListener listener : listeners) listener.scriptResumed(this);
			}
		} finally {
			lock.unlock();
		}	
	}

	public void stop() {
		for (IScriptListener listener : listeners) listener.scriptStopped(this, true);
	}

	public void suspend() {
		lock.lock();
		try {
			this.suspended = true;
		
			for (IScriptListener listener : listeners) listener.scriptPaused(this);
			suspend.await();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
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

}
