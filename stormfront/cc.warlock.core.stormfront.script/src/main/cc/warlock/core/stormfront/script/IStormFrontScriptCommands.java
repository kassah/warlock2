package cc.warlock.core.stormfront.script;

import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.stormfront.client.IStormFrontClient;

public interface IStormFrontScriptCommands extends IScriptCommands {

	public IStormFrontClient getStormFrontClient();
	
	public void waitForRoundtime();
	
}
