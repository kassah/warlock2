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
package cc.warlock.core.stormfront.script.javascript;

import java.util.HashMap;

import org.mozilla.javascript.Scriptable;

import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.javascript.IJavascriptVariableProvider;
import cc.warlock.core.script.javascript.JavascriptEngine;
import cc.warlock.core.script.javascript.JavascriptScript;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.script.IStormFrontScriptCommands;

public class StormFrontJavascriptVars implements IJavascriptVariableProvider {

	protected HashMap<JavascriptScript, StormFrontJavascriptCommands> scriptCommands =
		new HashMap<JavascriptScript, StormFrontJavascriptCommands>();
	
	public StormFrontJavascriptVars ()
	{
		JavascriptEngine engine = (JavascriptEngine)
			ScriptEngineRegistry.getScriptEngine(JavascriptEngine.ENGINE_ID);
		
		engine.addVariableProvider(this);
	}
	
	public void loadVariables(JavascriptScript script, Scriptable scope) {
		if (script.getClient() instanceof IStormFrontClient)
		{
			IStormFrontClient sfClient = (IStormFrontClient) script.getClient();
			
			//overwrite the "script" variable with our big delegator
			StormFrontJavascriptCommands commands = new StormFrontJavascriptCommands((IStormFrontScriptCommands)script.getCommands(), script);
			scriptCommands.put(script, commands);
			
			scope.put("script", scope, commands);
			scope.put("compass", scope, sfClient.getCompass());
			scope.put("commandHistory", scope, sfClient.getCommandHistory());
			
			scope.put("mana", scope, new JavascriptBarStatus(scope, sfClient.getMana()));
			scope.put("health", scope, new JavascriptBarStatus(scope, sfClient.getHealth()));
			scope.put("fatigue", scope, new JavascriptBarStatus(scope, sfClient.getFatigue()));
			scope.put("spirit", scope, new JavascriptBarStatus(scope, sfClient.getSpirit()));
			scope.put("roundtime", scope, new JavascriptProperty<Integer>(scope, sfClient.getRoundtime()));
			scope.put("leftHand", scope, new JavascriptProperty<String>(scope, sfClient.getLeftHand()));
			scope.put("rightHand", scope, new JavascriptProperty<String>(scope, sfClient.getRightHand()));
			scope.put("spell", scope, new JavascriptProperty<String>(scope, sfClient.getCurrentSpell()));
			
			scope.put("roomExits", scope, new JavascriptComponent(scope, sfClient, IStormFrontClient.COMPONENT_ROOM_EXITS));
			scope.put("roomObjects", scope, new JavascriptComponent(scope, sfClient, IStormFrontClient.COMPONENT_ROOM_OBJECTS));
			scope.put("roomDescription", scope, new JavascriptComponent(scope, sfClient, IStormFrontClient.COMPONENT_ROOM_DESCRIPTION));
			scope.put("roomPlayers", scope, new JavascriptComponent(scope, sfClient, IStormFrontClient.COMPONENT_ROOM_PLAYERS));
			scope.put("roomTitle", scope, new JavascriptProperty<String>(scope, sfClient.getRoomStream().getTitle()));
			
		}
	}
	
}
