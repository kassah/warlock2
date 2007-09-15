package cc.warlock.script;

public interface IScriptListener {
	
	public void scriptAdded (IScript script);
	
	public void scriptRemoved (IScript script);
	
	public void scriptStarted (IScript script);
	
	public void scriptPaused (IScript script);
	
	public void scriptResumed (IScript script);
	
	public void scriptStopped (IScript script, boolean userStopped);
}
