package cc.warlock.core.stormfront.serversettings.server;

import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.stormfront.client.IStormFrontClient;

public interface IServerScriptInfo extends IScriptInfo {

	public String getContents();
	
	public String getComment();
	
	public IStormFrontClient getClient();
	
}
