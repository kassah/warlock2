package cc.warlock.core.stormfront.script.javascript;

import java.util.Hashtable;

import org.mozilla.javascript.Scriptable;

import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptListener;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.javascript.IJavascriptVariableProvider;
import cc.warlock.core.script.javascript.JavascriptEngine;
import cc.warlock.core.script.javascript.JavascriptScript;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.script.internal.StormFrontScriptCommands;

public class StormFrontJavascriptVars implements IJavascriptVariableProvider, IScriptListener {

	protected Hashtable<JavascriptScript, StormFrontJavascriptCommands> scriptCommands =
		new Hashtable<JavascriptScript, StormFrontJavascriptCommands>();
	
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
			StormFrontScriptCommands sfCommands = new StormFrontScriptCommands(sfClient, script);
			StormFrontJavascriptCommands commands = new StormFrontJavascriptCommands(script.getCommands(), sfCommands);
			scriptCommands.put(script, commands);
			
			scope.put("script", scope, commands);
			scope.put("compass", scope, sfClient.getCompass());
			scope.put("commandHistory", scope, sfClient.getCommandHistory());
			
			scope.put("mana", scope, new JavascriptBarStatus(sfClient.getMana()));
			scope.put("health", scope, new JavascriptBarStatus(sfClient.getHealth()));
			scope.put("fatigue", scope, new JavascriptBarStatus(sfClient.getFatigue()));
			scope.put("spirit", scope, new JavascriptBarStatus(sfClient.getSpirit()));
			scope.put("roundtime", scope, new JavascriptProperty<Integer>(sfClient.getRoundtime()));
			scope.put("leftHand", scope, new JavascriptProperty<String>(sfClient.getLeftHand()));
			scope.put("rightHand", scope, new JavascriptProperty<String>(sfClient.getRightHand()));
			scope.put("spell", scope, new JavascriptProperty<String>(sfClient.getCurrentSpell()));
			
			scope.put("roomExits", scope, new JavascriptComponent(sfClient, IStormFrontClient.COMPONENT_ROOM_EXITS));
			scope.put("roomObjects", scope, new JavascriptComponent(sfClient, IStormFrontClient.COMPONENT_ROOM_OBJECTS));
			scope.put("roomDescription", scope, new JavascriptComponent(sfClient, IStormFrontClient.COMPONENT_ROOM_DESCRIPTION));
			scope.put("roomPlayers", scope, new JavascriptComponent(sfClient, IStormFrontClient.COMPONENT_ROOM_PLAYERS));
			scope.put("roomTitle", scope, new JavascriptProperty<String>(sfClient.getRoomStream().getTitle()));
			
			script.addScriptListener(this);
		}
	}

	public void scriptAdded(IScript script) {}
	public void scriptPaused(IScript script) {}
	public void scriptRemoved(IScript script) {}
	public void scriptResumed(IScript script) {}
	public void scriptStarted(IScript script) {}
	
	public void scriptStopped(IScript script, boolean userStopped) {
		// Cleanup on stop
		if (script instanceof JavascriptScript)
		{
			JavascriptScript jscript = (JavascriptScript)script;
			if (scriptCommands.containsKey(jscript)) {
				StormFrontJavascriptCommands commands = scriptCommands.get(jscript);
				commands.clearActions();
			}
		}
	}
	
}
