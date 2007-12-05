package cc.warlock.core.stormfront.script;

import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.script.wsl.WSLAbstractCommand;

public interface IStormFrontScriptCommands extends IScriptCommands {

	public IStormFrontClient getStormFrontClient();
	
	public void waitForRoundtime();
	
	public void addAction(WSLAbstractCommand action, String text);
	
	public void clearActions();
	
	public void removeAction(String text);
	
}
