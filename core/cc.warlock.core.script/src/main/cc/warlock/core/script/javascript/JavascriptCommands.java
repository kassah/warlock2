package cc.warlock.core.script.javascript;

import java.io.Serializable;
import java.util.Arrays;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.script.Match;
import cc.warlock.core.script.internal.TextMatch;

public class JavascriptCommands {

	protected IScriptCommands commands;
	protected JavascriptScript script;
	
	private static final String CALLBACK = "callback";
	private static final String USER_OBJECT = "userobject";
	
	public class JavascriptStopException extends Exception implements Serializable {
		private static final long serialVersionUID = 7226391328268718796L;
	}
	
	public JavascriptCommands(IScriptCommands commands, JavascriptScript script) {
		this.commands = commands;
		this.script = script;
	}

	public void echo(String text) {
		commands.echo(script, text);
	}

	public Match matchWait(Match[] matches) {
		for(Match m : matches) {
			commands.addMatch(m);
		}
		Match match = commands.matchWait();
		Function function = (Function)match.getAttribute(CALLBACK);
		try {
			function.call(script.getContext(), script.getScope(), null, new Object[] {match.getAttribute(USER_OBJECT)});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return match;
	}

	public void move(String direction) {
		commands.move(direction);
	}

	public void pause(int seconds) {
		commands.pause(seconds);
	}

	public void put(String text) {
		commands.put(script, text);
	}

	public void waitFor(Match match) {
		commands.waitFor(match);
	}

	public void waitForPrompt() {
		commands.waitForPrompt();
	}

	public void exit() throws JavascriptStopException {
		script.stop();
		
		throw new JavascriptStopException();
	}
	
	public Match match(String text, Function function, Scriptable object) {
		Match m = new TextMatch(text);
		m.setAttribute(CALLBACK, function);
		m.setAttribute(USER_OBJECT, object);
		
		return m;
	}
	
	public void stop() {
		commands.stop();
	}
}
