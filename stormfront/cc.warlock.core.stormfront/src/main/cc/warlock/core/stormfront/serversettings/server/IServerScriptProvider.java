package cc.warlock.core.stormfront.serversettings.server;

import cc.warlock.core.script.IScriptProvider;
import cc.warlock.core.stormfront.client.IStormFrontClient;

public interface IServerScriptProvider extends IScriptProvider {

	public IStormFrontClient getClient();
	
}
