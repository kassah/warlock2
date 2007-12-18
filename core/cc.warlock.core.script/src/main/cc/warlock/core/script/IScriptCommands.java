package cc.warlock.core.script;

import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IWarlockClient;

public interface IScriptCommands extends IRoomListener {

	public void put (String text);
	
	public void echo (String text);
	
	public void move (String direction);
	
	public void waitFor (Match match);
	
	public void waitNextRoom ();
	
	public void addMatch(Match match);
	
	public Match matchWait (double timeout);
	
	public void pause (double seconds);
	
	public void waitForPrompt ();
	
	public IWarlockClient getClient();
	
	public void stop();
	
	public void interrupt();
	
	public void clearInterrupt();
}
