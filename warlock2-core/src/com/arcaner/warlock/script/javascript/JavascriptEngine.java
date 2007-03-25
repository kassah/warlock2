/*
 * Created on Jan 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.script.javascript;

import java.io.FileReader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.script.IScript;
import com.arcaner.warlock.script.IScriptEngine;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JavascriptEngine implements IScriptEngine {

	public static final String ENGINE_ID = "com.arcaner.warlock.script.javascript.JavascriptEngine";
	
	public String getScriptEngineId() {
		return ENGINE_ID;
	}
	
	public String getScriptEngineName() {
		return "Standard Javascript Engine (c) 2005 Warlock Team";
	}
	
	public String[] getSupportedExtensions() {
		return new String[] { "js" };
	}
	
	public IScript startScript(final IWarlockClient client, String path) {
		final JavascriptScript script = new JavascriptScript (path);
		new Thread(new Runnable() {
			public void run () {
				Context context = Context.enter();
				try {
					Scriptable scope = context.initStandardObjects();
					scope.put("client", scope, client);
					
					Object result = context.evaluateReader(scope, new FileReader(script.getPath()), "<cmd>", 1, null);
					System.out.println("script result: " + Context.toString(result));
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
