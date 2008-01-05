package cc.warlock.core.script;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IWarlockClient;

public interface IScriptCommands extends IRoomListener {

	public void put (String text);
	
	public void echo (String text);
	
	public void move (String direction);
	
	public void waitFor (IMatch match);
	
	public void waitNextRoom ();
	
	public BlockingQueue<String> getLineQueue();
	
	public IMatch matchWait (Collection<IMatch> matches, BlockingQueue<String> matchQueue, double timeout);
	
	public void pause (double seconds);
	
	public void waitForPrompt ();
	
	public IWarlockClient getClient();
	
	public void stop();
	
	public void interrupt();
	
	public void clearInterrupt();
}
