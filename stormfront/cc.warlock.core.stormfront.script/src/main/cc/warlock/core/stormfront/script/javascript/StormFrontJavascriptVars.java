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
