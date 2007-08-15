package cc.warlock.script;

import cc.warlock.client.stormfront.IStormFrontClient;

public interface IScriptCommands {

	public void put (String text);
	public void put (IScript script, String text);
	
	public void echo (String text);
	public void echo (IScript script, String text);
	
	public void move (String direction, IScriptCallback callback);
	
	public void nextRoom (IScriptCallback callback);
	
	public void waitFor (String text, boolean regex, boolean ignoreCase, IScriptCallback callback);
	
	public void matchWait (IMatch[] matches, IScriptCallback callback);
	
	public void pause (int seconds);
	
	public void waitForLine (IScriptCallback callback);
	
	public void removeCallback (IScriptCallback callback);
	
	public IStormFrontClient getClient();
	
	public void movedToRoom();
}
