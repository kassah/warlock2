package cc.warlock.core.script.javascript;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.internal.RegexMatch;
import cc.warlock.core.script.internal.TextMatch;

public class MatchList extends ScriptableObject {

	private HashMap<IMatch, Runnable> matches = new HashMap<IMatch, Runnable>();
	private BlockingQueue<String> queue;
	private JavascriptScript script;
	private Scriptable userObject;
	
	public MatchList() {
	}
	
	public void jsConstructor() {
		JavascriptCommands jsCommands = (JavascriptCommands)this.getParentScope().get("script", this.getParentScope());
		script = jsCommands.getScript();
		queue = script.getCommands().getLineQueue();
	}
	
	private class JSTextMatchData implements Runnable {
		
		private Function function;
		
		public JSTextMatchData(Function function) {
			this.function = function;
		}
		
		public void run() {
			if (userObject == null) {
				function.call(script.getContext(), script.getScope(), null, new Object[] {});
			} else {
				function.call(script.getContext(), script.getScope(), null, new Object[] { userObject });
			}
		}
	}
	
	public void jsFunction_match(Function function, String text) {
		TextMatch match = new TextMatch(text, true);
		matches.put(match, new JSTextMatchData(function));
	}
	
	private class JSRegexMatchData implements Runnable {
		
		private Function function;
		private RegexMatch match;
		
		public JSRegexMatchData(Function function, RegexMatch match) {
			this.function = function;
			this.match = match;
		}
		
		public void run() {
			Context cx = script.getContext();
			Scriptable scope = script.getScope();
			Scriptable groups = cx.newArray(scope, match.groups().size());
			int i = 0;
			for(String str : match.groups()) {
				scope.put(i, groups, str);
				i++;
			}
			if (userObject == null) {
				function.call(cx, scope, null, new Object[] { groups });
			} else {
				function.call(cx, scope, null, new Object[] { groups, userObject });
			}
		}
	}
	
	public void jsFunction_matchRe(Function function, String regex) {
		RegexMatch match = new RegexMatch(regex);
		matches.put(match, new JSRegexMatchData(function, match));
	}
	
	public IMatch jsFunction_matchWait() {
		IMatch match = script.getCommands().matchWait(matches.keySet(), queue, 0.0);
		
		if (match != null)
		{
			script.getCommands().echo("Got a match!");
			matches.get(match).run();
		}
		
		return match;
	}
	
	@Override
	public String getClassName() {
		return "MatchList";
	}

}
