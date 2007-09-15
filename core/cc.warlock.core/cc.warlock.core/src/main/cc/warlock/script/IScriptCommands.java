package cc.warlock.script;

import java.util.List;

import cc.warlock.client.IWarlockClient;

public interface IScriptCommands {

	public void put (String text);
	public void put (IScript script, String text);
	
	public void echo (String text);
	
	public void echo (IScript script, String text);
	
	public void move (String direction);
	
	public void nextRoom ();
	
	public void waitFor (Match match);
	
	public Match matchWait (List<Match> matches);
	
	public void pause (int seconds);
	
	public void waitForPrompt ();
	
	public IWarlockClient getClient();
	
	public void movedToRoom();
	
	public void stop();
}
