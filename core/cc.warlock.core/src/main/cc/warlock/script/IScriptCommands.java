package cc.warlock.script;

import cc.warlock.client.stormfront.IStormFrontClient;

public interface IScriptCommands {

	public void put (String text);
	public void put (IScript script, String text);
	
	public void echo (String text);
	
	public void echo (IScript script, String text);
	
	public void move (String direction);
	
	public void nextRoom ();
	
	public void waitFor (Match match);
	
	public void waitForRoundtime ();
	
	public Match matchWait (Match[] matches);
	
	public void pause (int seconds);
	
	public void waitForPrompt ();
	
	public IStormFrontClient getClient();
	
	public void movedToRoom();
	
	public void stop();
}
