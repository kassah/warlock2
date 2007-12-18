package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptListener;

public class SWTScriptListener implements IScriptListener {

	protected IScriptListener listener;
	
	public SWTScriptListener (IScriptListener listener)
	{
		this.listener = listener;
	}
	
	private class AddedListener implements Runnable
	{
		private IScript script;
		
		public AddedListener(IScript script) {
			this.script = script;
		}
		
		public void run() {
			listener.scriptAdded(script);
		}
	}
	
	private class RemovedListener implements Runnable
	{
		private IScript script;
		
		public RemovedListener(IScript script) {
			this.script = script;
		}
		
		public void run() {
			listener.scriptRemoved(script);
		}
	}
	
	private class PausedListener implements Runnable
	{
		private IScript script;
		
		public PausedListener(IScript script) {
			this.script = script;
		}
		
		public void run() {
			listener.scriptPaused(script);
		}
	}
	
	private class ResumedListener implements Runnable
	{
		private IScript script;
		
		public ResumedListener(IScript script) {
			this.script = script;
		}
		
		public void run() {
			listener.scriptResumed(script);
		}
	}
	
	private class StartedListener implements Runnable
	{
		private IScript script;
		
		public StartedListener(IScript script) {
			this.script = script;
		}
		
		public void run() {
			listener.scriptStarted(script);
		}
	}
	
	private class StoppedListener implements Runnable
	{
		private IScript script;
		private boolean userStopped;
		
		public StoppedListener(IScript script, boolean userStopped) {
			this.script = script;
			this.userStopped = userStopped;
		}
		
		public void run() {
			listener.scriptStopped(script, userStopped);
		}
	}
	
	protected void run(Runnable runnable)
	{
		Display.getDefault().asyncExec(runnable);
	}
	
	public void scriptAdded(IScript script) {
		run(new AddedListener(script));
	}
	
	public void scriptRemoved(IScript script) {
		run(new RemovedListener(script));
	}
	
	public void scriptPaused(IScript script) {
		run(new PausedListener(script));
	}

	public void scriptResumed(IScript script) {
		run(new ResumedListener(script));
	}

	public void scriptStarted(IScript script) {
		run(new StartedListener(script));
	}

	public void scriptStopped(IScript script, boolean userStopped) {
		run(new StoppedListener(script, userStopped));
	}

}
