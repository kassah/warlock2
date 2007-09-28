package cc.warlock.core.script;

import java.util.List;

import cc.warlock.core.client.IWarlockClient;

public interface IScriptProvider {

	public List<IScriptInfo> getScriptInfos();
	
	public IScript startScript (IScriptInfo scriptInfo, IWarlockClient client, String[] arguments);
	
}
