package cc.warlock.core.stormfront.script.javascript;

import org.mozilla.javascript.Scriptable;

import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.javascript.IJavascriptVariableProvider;
import cc.warlock.core.script.javascript.JavascriptEngine;
import cc.warlock.core.script.javascript.JavascriptScript;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.script.IStormFrontScriptCommands;
import cc.warlock.core.stormfront.script.internal.StormFrontScriptCommands;

public class StormFrontJavascriptVars implements IJavascriptVariableProvider {

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
			IStormFrontScriptCommands sfCommands = new StormFrontScriptCommands(sfClient, script);
			StormFrontJavascriptCommands commands = new StormFrontJavascriptCommands(script.getCommands(), sfCommands);
			scope.put("script", scope, commands);
			scope.put("compass", scope, sfClient.getCompass());
			scope.put("commandHistory", scope, sfClient.getCommandHistory());
		}
	}

}
