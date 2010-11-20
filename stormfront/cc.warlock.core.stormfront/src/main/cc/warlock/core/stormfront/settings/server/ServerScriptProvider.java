/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.core.stormfront.settings.server;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.stormfront.client.IStormFrontClient;

public class ServerScriptProvider implements IServerScriptProvider
{
	protected HashMap<String, ServerScriptInfo> scripts = new HashMap<String, ServerScriptInfo>();
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
		
		public ServerScript getServerScript() {
			return script;
		}
		
		public Reader openReader() {
			return new StringReader(script.getScriptContents());
		}
		
		public IStormFrontClient getClient() {
			return ServerScriptProvider.this.client;
		}
		
		public IServerScriptProvider getProvider() {
			return ServerScriptProvider.this;
		}
	}
	
	public ServerScriptProvider (IStormFrontClient client)
	{
		this.client = client;
	}
	
	public IStormFrontClient getClient() {
		return client;
	}
	
	public List<IScriptInfo> getScriptInfos() {
		return Arrays.asList(scripts.values().toArray(new IScriptInfo[scripts.values().size()]));
	}
	
	public IScript startScript (IScriptInfo scriptInfo, IWarlockClientViewer viewer, String[] arguments)
	{
		for (IScriptEngine engine : ScriptEngineRegistry.getScriptEngines())
		{
			if (engine.supports(scriptInfo))
			{
				return engine.startScript(scriptInfo, viewer, arguments);
			}
		}
		return null;
	}
	
	public void scriptContentsUpdated (ServerScript script)
	{
		if (!scripts.containsKey(script.getName()))
		{
			ServerScriptInfo info = new ServerScriptInfo();
			info.script = script;
			
			scripts.put(script.getName(), info);
		}
	}
}
