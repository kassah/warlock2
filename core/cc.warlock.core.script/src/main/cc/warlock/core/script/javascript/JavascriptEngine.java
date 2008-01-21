/*
 * Created on Jan 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.script.javascript;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrappedException;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.IScriptFileInfo;
import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.script.configuration.ScriptConfiguration;


/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JavascriptEngine implements IScriptEngine {

	public static final String ENGINE_ID = "cc.warlock.script.javascript.JavascriptEngine";
	protected ArrayList<IJavascriptVariableProvider> varProviders = new ArrayList<IJavascriptVariableProvider>();
	protected ArrayList<JavascriptScript> runningScripts = new ArrayList<JavascriptScript>();
	
	public Scriptable scope;
	
	private class WarlockContextFactory extends ContextFactory {
		
		protected Context makeContext() {
			Context cx = new Context();
			cx.setOptimizationLevel(-1);
			cx.setInstructionObserverThreshold(1000);
			return cx;
		}
		
		protected void observeInstructionCount(Context cx, int instructionCount) {
			JavascriptScript script = null;
			for(JavascriptScript cur : runningScripts) {
				if(cur.getContext().equals(cx)) {
					script = cur;
					break;
				}
			}
			if(script == null) {
				System.out.println("Couldn't find context.");
				return;
			}
			if (!script.isRunning()) {
				throw new Error();
			}
		}
	}
	
	public JavascriptEngine() {
		ContextFactory.initGlobal(new WarlockContextFactory());
	}
	
	public String getScriptEngineId() {
		return ENGINE_ID;
	}
	
	public String getScriptEngineName() {
		return "Standard Javascript Engine (c) 2002-2007 Warlock Team";
	}
	
	
	public void addVariableProvider (IJavascriptVariableProvider provider)
	{
		varProviders.add(provider);
	}
	
	public void removeVariableProvider (IJavascriptVariableProvider provider)
	{
		if (varProviders.contains(provider))
			varProviders.remove(provider);
	}
	
	public boolean supports(IScriptInfo scriptInfo) {
		if (scriptInfo instanceof IScriptFileInfo)
		{
			IScriptFileInfo info = (IScriptFileInfo) scriptInfo;
			if (info.getExtension() != null)
			{
				List<String> extensions = ScriptConfiguration.instance().getEngineExtensions(ENGINE_ID);
				if (extensions != null && extensions.contains(info.getExtension().toLowerCase())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	protected static String includeFunction = "function include (src) { script.include(src); }";
	protected static String includeFunctionName = "<warlock js:include>";
	

	
	public IScript startScript(IScriptInfo info, IWarlockClient client, final String[] arguments) {
		final JavascriptScript script = new JavascriptScript(this, info, client);
		
		script.start();
		runningScripts.add(script);
		
		new Thread(new Runnable() {
			
			public void run () {
				Context context = Context.enter();
				try {
					script.setContext(context);
					
					scope = context.initStandardObjects();
					scope.put("script", scope, script.getCommands());
					scope.put("arguments", scope, arguments);

					Function function = context.compileFunction(scope, includeFunction, includeFunctionName, 0, null);
					scope.put("include", scope, function);
					
					for (IJavascriptVariableProvider provider : varProviders) {
						provider.loadVariables(script, scope);
					}
					ScriptableObject.defineClass(scope, MatchList.class);
					
					Reader reader = script.getScriptInfo().openReader();
					Object result = context.evaluateReader(scope, reader, script.getName(), 1, null);
					System.out.println("script result: " + Context.toString(result));
					script.getClient().getDefaultStream().echo("[script finished: " + script.getName() + "]\n");
					reader.close();
				}
				catch (WrappedException e) {
					if (!(e.getCause() instanceof Error))
					{
						e.printStackTrace();
						script.getClient().getDefaultStream().echo(
								"[JS " + e.details() + " at line " + e.lineNumber() + "]\n"
								+ "[script terminated: "+ script.getName()+"]\n"
							);
					}
				}
				catch (RhinoException e) {
					script.getClient().getDefaultStream().echo(
							"[JS " + e.details() + " at line " + e.lineNumber() + "]\n"
							+ "[script terminated: "+ script.getName()+"]\n"
						);
				}
				catch(Error e) {
					script.getClient().getDefaultStream().echo("[script terminated by user " + script.getName() + "]\n");
					e.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
					script.getClient().getDefaultStream().echo("[script terminated with error: " + script.getName() + "]\n");
				}
				finally {
					if(script.isRunning()) {
						script.stop();
					}
					Context.exit();
					runningScripts.remove(script);
				}
			}
		}).start();
		return script;
	}

	public static Object loadString (String content)
	{
		Context context = Context.enter();
		try {
			
			Scriptable scope = context.initStandardObjects();
			Object result = context.evaluateString(scope, content, "<cmd>", 1, null);
			
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			Context.exit();
		}
	}
	
	public static void main (String args[])
	{
		loadString("function helloWorld () { return 'Hello World'; } helloWorld(); ");
	}
	
	public Collection<? extends IScript> getRunningScripts() {
		return runningScripts;
	}
}
