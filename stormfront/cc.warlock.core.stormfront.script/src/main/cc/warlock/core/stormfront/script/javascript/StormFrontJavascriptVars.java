package cc.warlock.core.stormfront.script.javascript;

import org.mozilla.javascript.Scriptable;

import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.javascript.IJavascriptVariableProvider;
import cc.warlock.core.script.javascript.JavascriptCommands;
import cc.warlock.core.script.javascript.JavascriptEngine;
import cc.warlock.core.script.javascript.JavascriptScript;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.script.IStormFrontScriptCommands;

public class StormFrontJavascriptVars implements IJavascriptVariableProvider {

	public StormFrontJavascriptVars ()
	{
		JavascriptEngine engine = (JavascriptEngine)
			ScriptEngineRegistry.getScriptEngine(JavascriptEngine.ENGINE_ID);
		
		engine.addVariableProvider(this);
	}
	
	protected class SFJavascriptCommands extends JavascriptCommands
	{
		public SFJavascriptCommands (IScriptCommands commands, JavascriptScript script)
		{
			super(commands,script);
		}
		
		public void waitForRoundtime ()	
		{
			((IStormFrontScriptCommands)commands).waitForRoundtime();
		}
	}
	
	public void loadVariables(JavascriptScript script, Scriptable scope) {
		if (script.getClient() instanceof IStormFrontClient)
		{
			IStormFrontClient sfClient = (IStormFrontClient) script.getClient();
			
			scope.put("compass", scope, sfClient.getCompass());
			scope.put("commandHistory", scope, sfClient.getCommandHistory());
		}
	}

}
