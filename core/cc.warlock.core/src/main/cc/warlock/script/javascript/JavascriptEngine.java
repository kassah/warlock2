/*
 * Created on Jan 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.script.javascript;

import java.io.Reader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrappedException;

import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.script.IScript;
import cc.warlock.script.IScriptCommands;
import cc.warlock.script.IScriptEngine;


/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JavascriptEngine implements IScriptEngine {

	public static final String ENGINE_ID = "cc.warlock.script.javascript.JavascriptEngine";
	public Scriptable scope;
	
	public String getScriptEngineId() {
		return ENGINE_ID;
	}
	
	public String getScriptEngineName() {
		return "Standard Javascript Engine (c) 2002-2007 Warlock Team";
	}
	
	public String[] getSupportedExtensions() {
		return new String[] { "js" };
	}
	
	public IScript startScript(final IScriptCommands commands, final String scriptName, final Reader scriptReader, final String[] arguments) {
		final JavascriptScript script = new JavascriptScript (commands, scriptName, this);
		new Thread(new Runnable() {
			public void run () {
				Context context = Context.enter();
				try {
					scope = context.initStandardObjects();
					scope.put("script", scope, script.getCommands());
					scope.put("arguments", scope, arguments);
					
					if (commands.getClient() instanceof IStormFrontClient)
					{
						IStormFrontClient sfClient = (IStormFrontClient) commands.getClient();
						scope.put("compass", scope, sfClient.getCompass());
						scope.put("commandHistory", scope, sfClient.getCommandHistory());
					}
					
					Object result = context.evaluateReader(scope, scriptReader, scriptName, 1, null);
					System.out.println("script result: " + Context.toString(result));
				}
				catch (WrappedException e) {
					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					Context.exit();
				}
			}
		}).start();
		
		return script;
	}

	public static void loadString (String content)
	{
		Context context = Context.enter();
		try {
			
			Scriptable scope = context.initStandardObjects();
			Object result = context.evaluateString(scope, content, "<cmd>", 1, null);
			
			System.out.println("script result: " + Context.toString(result));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			Context.exit();
		}
	}
	
	public static void main (String args[])
	{
		loadString("function helloWorld () { return 'Hello World'; } helloWorld(); ");
	}
}
