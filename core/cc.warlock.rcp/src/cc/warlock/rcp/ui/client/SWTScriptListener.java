package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptListener;

public class SWTScriptListener implements IScriptListener {

	protected IScriptListener listener;
	protected ListenerWrapper wrapper;
	protected boolean asynch;
	
	public SWTScriptListener (IScriptListener listener)
	{
		this(listener, false);
	}
	
	public SWTScriptListener (IScriptListener listener, boolean asynch)
	{
		this.listener = listener;
		this.wrapper = new ListenerWrapper();
		this.asynch = asynch;
	}
	
	private static enum EventType {
		Added, Removed, Paused, Resumed, Started, Stopped
	}
	
	private class ListenerWrapper implements Runnable
	{
		public IScript script;
		public EventType eventType;
		public boolean userStopped;
		
		public void run() {
			switch (eventType)
			{
			case Added: listener.scriptAdded(script); break;
			case Removed: listener.scriptRemoved(script); break;
			case Paused: listener.scriptPaused(script); break;
			case Resumed: listener.scriptResumed(script); break;
			case Started: listener.scriptStarted(script); break;
			case Stopped: listener.scriptStopped(script, userStopped); break;
			}
			script = null;
			eventType = null;
			userStopped = false;
		}
	}
	
	protected void run(Runnable runnable)
	{
		if (asynch)
		{
			Display.getDefault().asyncExec(runnable);
		} else {
			Display.getDefault().syncExec(runnable);
		}
	}
	
	public void scriptAdded(IScript script) {
		wrapper.script = script;
		wrapper.eventType = EventType.Added;
		run(wrapper);
	}
	
	public void scriptRemoved(IScript script) {
		wrapper.script = script;
		wrapper.eventType = EventType.Removed;
		run(wrapper);
	}
	
	public void scriptPaused(IScript script) {
		wrapper.script = script;
		wrapper.eventType = EventType.Paused;
		run(wrapper);
	}

	public void scriptResumed(IScript script) {
		wrapper.script = script;
		wrapper.eventType = EventType.Resumed;
		run(wrapper);
	}

	public void scriptStarted(IScript script) {
		wrapper.script = script;
		wrapper.eventType = EventType.Started;
		run(wrapper);
	}

	public void scriptStopped(IScript script, boolean userStopped) {
		wrapper.script = script;
		wrapper.userStopped = userStopped;
		wrapper.eventType = EventType.Stopped;
		run(wrapper);
	}

}
