package cc.warlock.core.script.javascript;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.internal.RegexMatch;
import cc.warlock.core.script.internal.TextMatch;

public class MatchList extends ScriptableObject {

	private HashMap<IMatch, Runnable> matches = new HashMap<IMatch, Runnable>();
	private BlockingQueue<String> queue;
	private JavascriptScript script;
	
	public MatchList() {
	}
	
	public void jsConstructor() {
		JavascriptCommands jsCommands = (JavascriptCommands)this.getParentScope().get("script", this.getParentScope());
		script = jsCommands.getScript();
		queue = script.getCommands().getLineQueue();
	}
	
	private class JSMatchData implements Runnable {
		
		private Function function;
		private String expression;
		protected Object[] args = new Object[0];
		
		public JSMatchData(Function function, String expression) {
			this.function = function;
			this.expression = expression;
		}
		
		public void run() {
			if(function != null) {
				function.call(script.getContext(), script.getScope(), null, args);
			}
			if(expression != null) {
				Script jsCommand = script.getContext().compileString(expression, "matchWait", 0, null);

				jsCommand.exec(script.getContext(), script.getScope());
			}
		}
	}
	
	public IMatch jsFunction_match(String text) {
		if(!script.isRunning()) {
			throw new Error();
		}
		TextMatch match = new TextMatch(text, true);
		matches.put(match, new JSMatchData(null, null));
		
		return match;
	}
	
	public IMatch jsFunction_match(String text, Function function) {
		if(!script.isRunning()) {
			throw new Error();
		}
		TextMatch match = new TextMatch(text, true);
		matches.put(match, new JSMatchData(function, null));
		
		return match;
	}
	
//	public IMatch jsFunction_match(String text, String expression) {
//		TextMatch match = new TextMatch(text, true);
//		matches.put(match, new JSMatchData(null, expression));
//		
//		return match;
//	}
	
	private class JSRegexMatchData extends JSMatchData {
		
		private RegexMatch match;
		
		public JSRegexMatchData(RegexMatch match, Function function, String expression) {
			super(function, expression);
			this.match = match;
		}
		
		public void run() {
			Scriptable groups = script.getContext().newArray(script.getScope(), match.groups().size());
			int i = 0;
			for(String str : match.groups()) {
				script.getScope().put(i, groups, str);
				i++;
			}
			
			args = new Object[] { groups };
			super.run();
		}
	}
	
	public IMatch jsFunction_matchRe(String regex) {
		if(!script.isRunning()) {
			throw new Error();
		}
		RegexMatch match = new RegexMatch(regex);
		matches.put(match, new JSRegexMatchData(match, null, null));
		
		return match;
	}
	
	public IMatch jsFunction_matchRe(String regex, Function function) {
		if(!script.isRunning()) {
			throw new Error();
		}
		RegexMatch match = new RegexMatch(regex);
		matches.put(match, new JSRegexMatchData(match, function, null));
		
		return match;
	}
	
//	public IMatch jsFunction_matchRe(String regex, String expression) {
//		RegexMatch match = new RegexMatch(regex);
//		matches.put(match, new JSRegexMatchData(match, null, expression));
//		
//		return match;
//	}
	
	public IMatch jsFunction_matchWait() {
		if(!script.isRunning()) {
			throw new Error();
		}
		IMatch match = script.getCommands().matchWait(matches.keySet(), queue, 0.0);
		
		if (match != null)
		{
//			script.getCommands().echo("Got a match!");
			matches.get(match).run();
		}
		
		return match;
	}
	
	@Override
	public String getClassName() {
		return "MatchList";
	}

}
