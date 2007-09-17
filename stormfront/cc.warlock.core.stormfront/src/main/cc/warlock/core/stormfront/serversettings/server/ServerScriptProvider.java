package cc.warlock.core.stormfront.serversettings.server;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.stormfront.client.IStormFrontClient;

public class ServerScriptProvider implements IServerScriptProvider
{
	protected Hashtable<String, IScript> scripts = new Hashtable<String, IScript>();
	protected Hashtable<String, ServerScriptInfo> infos = new Hashtable<String, ServerScriptInfo>();
	protected IStormFrontClient client;
	
	protected class ServerScriptInfo implements IServerScriptInfo
	{
		public ServerScript script;
		
		public String getContents() {
			return script.getScriptContents();
		}
		
		public String getScriptName() {
			return script.getName();
		}
		
		public String getComment () {
			return script.getComment();
		}
	}
	
	public ServerScriptProvider (IStormFrontClient client)
	{
		this.client = client;
	}
	
	public IStormFrontClient getClient() {
		return client;
	}
	
	public List<IScript> getScripts() {
		return Arrays.asList(scripts.values().toArray(new IScript[scripts.values().size()]));
	}
	
	protected IScript createScript (ServerScriptInfo info)
	{
		IScript script = null;
		for (IScriptEngine engine : ScriptEngineRegistry.getScriptEngines())
		{
			if (engine.supports(info))
			{
				StringReader reader = new StringReader(info.script.getScriptContents());
				script = engine.createScript(info.script.getName(), reader);
				reader.close();
				break;
			}
		}
		return script;
	}
	
	public void scriptContentsUpdated (ServerScript script)
	{
		if (! infos.containsKey(script.getName()))
		{
			ServerScriptInfo info = new ServerScriptInfo();
			info.script = script;
			
			infos.put(script.getName(), info);
		}
		scripts.put(script.getName(), createScript(infos.get(script.getName())));
	}
}
