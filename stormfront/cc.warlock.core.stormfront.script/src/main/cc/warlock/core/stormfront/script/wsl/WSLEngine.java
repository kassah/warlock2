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
package cc.warlock.core.stormfront.script.wsl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.IScriptFileInfo;
import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.script.IScriptListener;
import cc.warlock.core.script.configuration.ScriptConfiguration;
import cc.warlock.core.stormfront.client.IStormFrontClient;


public class WSLEngine implements IScriptEngine, IScriptListener {

	public static final String ENGINE_ID = "cc.warlock.script.wsl.WSLEngine";
	protected ArrayList<IScript> runningScripts = new ArrayList<IScript>();
	protected ArrayList<IWSLCommandDefinitionProvider> commandProviders = new ArrayList<IWSLCommandDefinitionProvider>();
	
	public WSLEngine ()
	{
		List<String> fileExtensions = ScriptConfiguration.instance().getEngineExtensions(ENGINE_ID);
		if (fileExtensions.size() == 0)
		{
			ScriptConfiguration.instance().addEngineExtension(ENGINE_ID, "wiz");
			ScriptConfiguration.instance().addEngineExtension(ENGINE_ID, "cmd");
			ScriptConfiguration.instance().addEngineExtension(ENGINE_ID, "wsl");
		}
	}
	
	public void addCommandProvider (IWSLCommandDefinitionProvider provider)
	{
		if (!commandProviders.contains(provider))
			commandProviders.add(provider);
	}
	
	public Collection<IWSLCommandDefinitionProvider> getCommandProviders() {
		return commandProviders;
	}
	
	public String getScriptEngineId() {
		return ENGINE_ID;
	}
	
	public String getScriptEngineName() {
		return "Standard Wizard Scripting Language Engine (c) 2002-2007 Warlock Team";
	}
	
	public boolean supports(IScriptInfo scriptInfo) {
		if (scriptInfo instanceof IScriptFileInfo)
		{
			IScriptFileInfo info = (IScriptFileInfo) scriptInfo;
			if (info.getExtension() != null)
			{
				List<String> extensions = ScriptConfiguration.instance().getEngineExtensions(ENGINE_ID);
				if (extensions.contains(info.getExtension().toLowerCase())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public IScript startScript(IScriptInfo info, IWarlockClient client, String[] arguments) {

		WSLScript wslScript = new WSLScript(this, info, (IStormFrontClient)client);

		runningScripts.add(wslScript);
		wslScript.addScriptListener(this);
		wslScript.start(Arrays.asList(arguments));
		return wslScript;
	}
	
	public void scriptResumed(IScript script) { }
	public void scriptPaused(IScript script) { }
	public void scriptStarted(IScript script) { }
	
	public void scriptStopped(IScript script, boolean userStopped) {
		runningScripts.remove(script);
	}
	
	public Collection<? extends IScript> getRunningScripts() {
		return runningScripts;
	}
}
