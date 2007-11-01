package cc.warlock.core.script;

import cc.warlock.core.client.IWarlockClient;

public interface IScriptCommands {

	public void put (String text);
	
	public void echo (String text);
	
	public void move (String direction);
	
	public void nextRoom ();
	
	public void waitFor (Match match);
	
	public void addMatch(Match match);
	
	public Match matchWait ();
	
	public void pause (int seconds);
	
	public void waitForPrompt ();
	
	public IWarlockClient getClient();
	
	public void movedToRoom();
	
	public void stop();
	
	public void interrupt();
	
	public void clearInterrupt();
}
