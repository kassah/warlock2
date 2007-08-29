package cc.warlock.script.javascript;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;

import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.script.CallbackEvent;
import cc.warlock.script.IScript;
import cc.warlock.script.IScriptCallback;
import cc.warlock.script.IScriptCommands;
import cc.warlock.script.Match;

public class JavascriptCommands implements IScriptCommands, IScriptCallback {

	private IScriptCommands commands;
	private JavascriptScript script;
	
	public class JavascriptStopException extends Exception {
		
	}
	
	public JavascriptCommands(IScriptCommands commands, JavascriptScript script) {
		this.commands = commands;
		this.script = script;
	}

	public void echo(String text) {
		commands.echo(text);
	}

	public void echo(IScript script, String text) {
		commands.echo(script, text);
	}

	public IStormFrontClient getClient() {
		return commands.getClient();
	}

	public Match matchWait(Match[] matches) {
		return commands.matchWait(matches);
	}

	public void move(String direction, IScriptCallback callback) {
		commands.move(direction, callback);
	}

	public void movedToRoom() {
		commands.movedToRoom();
	}

	public void nextRoom(IScriptCallback callback) {
		commands.nextRoom(callback);
	}

	public void pause(int seconds) {
		commands.pause(seconds);
	}

	public void put(String text) {
		commands.put(text);
	}

	public void put(IScript script, String text) {
		commands.put(script, text);
	}

	public void waitFor(Match match) {
		commands.waitFor(match);
	}

	public void waitForPrompt(IScriptCallback callback) {
		commands.waitForPrompt(callback);
	}

	public void waitForRoundtime(IScriptCallback callback) {
		commands.waitForRoundtime(callback);
	}

	public void exit() throws JavascriptStopException {
		throw new JavascriptStopException();
	}
	
	public Match match(String text, Function function) {
		Match m = new Match();
		m.setMatchText(text);
		m.setAttribute("callback", function);
		
		return m;
	}
	
	public void handleCallback(CallbackEvent event) {
		if(event.type == CallbackType.Matched) {
			Match match = (Match)event.data.get(CallbackEvent.DATA_MATCH);
			Function function = (Function)match.getAttribute("callback");
			Context cx = Context.enter();
			try {
				function.call(cx, script.getScope(), null, new Object[] {});
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				Context.exit();
			}
		}
	}
}
