package cc.warlock.core.stormfront.serversettings.server;

import java.io.Reader;
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
		
		public Reader openReader() {
			return new StringReader(script.getScriptContents());
		}
		
		public IStormFrontClient getClient() {
			return ServerScriptProvider.this.client;
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
				script = engine.createScript(info);
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
		
		IScript newScript = createScript(infos.get(script.getName()));
		if (newScript != null)
		{
			scripts.put(script.getName(), newScript);
		}
	}
}
