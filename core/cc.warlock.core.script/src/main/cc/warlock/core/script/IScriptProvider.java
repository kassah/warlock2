package cc.warlock.core.script;

import java.util.List;

import cc.warlock.core.client.IWarlockClient;

public interface IScriptProvider {

	public List<String> getScriptNames();
	
	public IScript startScript (String scriptName, IWarlockClient client, String[] arguments);
	
}
