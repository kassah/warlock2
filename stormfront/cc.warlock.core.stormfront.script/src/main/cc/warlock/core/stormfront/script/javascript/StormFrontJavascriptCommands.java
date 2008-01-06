/**
 * 
 */
package cc.warlock.core.stormfront.script.javascript;

import java.util.Collection;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;

import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.internal.RegexMatch;
import cc.warlock.core.script.javascript.JavascriptCommands;
import cc.warlock.core.script.javascript.JavascriptScript;
import cc.warlock.core.stormfront.script.IStormFrontScriptCommands;

public class StormFrontJavascriptCommands extends JavascriptCommands
{
	protected IStormFrontScriptCommands sfCommands;
	
	public StormFrontJavascriptCommands (IStormFrontScriptCommands commands, JavascriptScript script)
	{
		super(commands, script);
		this.sfCommands = commands;
	}

	protected class JSActionHandler implements Runnable 
	{
		private Function function;
		private IMatch match;
		
		public JSActionHandler (Function function, RegexMatch match)
		{
			this.function = function;
			this.match = match;
		}
		
		public void run() {
			Context.enter();
			try {
				Object[] arguments = new Object[0];
				Collection<String> matchGroups = ((RegexMatch)match).groups();
				arguments = matchGroups.toArray(new String[matchGroups.size()]);
				
				function.call(script.getContext(), script.getScope(), null, arguments);
			} finally {
				Context.exit();
			}
		}
	}
	
	// IStormFrontScriptCommands delegated methods
	public void addAction(Function action, String text) {
		RegexMatch match = new RegexMatch(text);
		JSActionHandler command = new JSActionHandler(action, match);
		sfCommands.addAction(command, match);
	}

	public void removeAction(String text) {
		sfCommands.removeAction(text);
	}
	
	public void removeAction(IMatch action)
	{
		sfCommands.removeAction(action);
	}

	public void clearActions() {
		sfCommands.clearActions();
	}

	public void nextRoom() {
		sfCommands.nextRoom();
	}

	public void pause(double seconds) {
		sfCommands.pause(seconds);
	}

	public void waitForRoundtime() {
		sfCommands.waitForRoundtime();
	}

	public void waitNextRoom() {
		sfCommands.waitNextRoom();
	}

}