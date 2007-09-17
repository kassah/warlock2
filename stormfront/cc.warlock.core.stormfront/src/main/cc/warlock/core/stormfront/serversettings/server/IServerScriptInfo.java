package cc.warlock.core.stormfront.serversettings.server;

import cc.warlock.core.script.IScriptInfo;

public interface IServerScriptInfo extends IScriptInfo {

	public String getContents();
	
	public String getComment();
	
}
