package cc.warlock.core.stormfront.script;

import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.stormfront.client.IStormFrontClient;

public interface IStormFrontScriptCommands extends IScriptCommands {

	public IStormFrontClient getStormFrontClient();
	
	public void waitForRoundtime();
	
	public void addAction(Runnable action, IMatch match);
	
	public void clearActions();
	
	public void removeAction(IMatch action);
	
	public void removeAction(String text);
	
}
