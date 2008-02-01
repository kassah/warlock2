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
package cc.warlock.core.script;

import java.util.ArrayList;
import java.util.List;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.script.internal.FilesystemScriptProvider;


public class ScriptEngineRegistry {

	private static ArrayList<IScriptEngine> engines = new ArrayList<IScriptEngine>();
	private static ArrayList<IScriptProvider> providers = new ArrayList<IScriptProvider>();
	
	static {
		// force initialization of filesystem script provider
		FilesystemScriptProvider.instance();
	}
	
	public static void addScriptProvider (IScriptProvider provider)
	{
		providers.add(provider);
	}
	
	public static void removeScriptProvider (IScriptProvider provider)
	{
		if (providers.contains(provider))
			providers.remove(provider);
	}
	
	public static void addScriptEngine (IScriptEngine engine)
	{
		engines.add(engine);
	}
	
	public static void removeScriptEngine (IScriptEngine engine)
	{
		engines.remove(engine);
	}
	
	public static List<IScriptEngine> getScriptEngines ()
	{
		return engines;
	}
	
	public static List<IScriptProvider> getScriptProviders ()
	{
		return providers;
	}
	
	public static IScriptProvider getScriptProvider (Class<? extends IScriptProvider> clazz)
	{
		for (IScriptProvider provider : providers)
		{
			if (clazz.isInstance(provider))
				return provider;
		}
		return null;
	}
	
	public static IScriptEngine getScriptEngine (String engineId)
	{
		for (IScriptEngine engine : engines)
		{
			if (engine.getScriptEngineId().equals(engineId))
				return engine;
		}
		
		return null;
	}
	
	public static IScript startScript (String scriptName, IWarlockClient client, String[] arguments)
	{
		for (IScriptProvider provider : providers)
		{
			for (IScriptInfo scriptInfo : provider.getScriptInfos())
			{
				if (scriptInfo.getScriptName().equalsIgnoreCase(scriptName)) {
					IScript script = provider.startScript(scriptInfo, client, arguments);
					if(script != null)
						return script;
				}
			}
		}
		
		client.getDefaultStream().echo("Could not find script \"" + scriptName + "\"\n");
		return null;
	}
	
	public static List<IScript> getRunningScripts ()
	{
		ArrayList<IScript> scripts = new ArrayList<IScript>();
		for (IScriptEngine engine : engines)
		{
			scripts.addAll(engine.getRunningScripts());
		}
		return scripts;
	}
}
