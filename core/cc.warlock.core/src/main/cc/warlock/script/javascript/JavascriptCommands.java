package cc.warlock.script.javascript;

import org.mozilla.javascript.Function;

import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.script.IScript;
import cc.warlock.script.IScriptCommands;
import cc.warlock.script.Match;

public class JavascriptCommands implements IScriptCommands {

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
		/*Match match = (Match)event.data.get(CallbackEvent.DATA_MATCH);
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
		}*/
	}

	public void move(String direction) {
		commands.move(direction);
	}

	public void movedToRoom() {
		commands.movedToRoom();
	}

	public void nextRoom() {
		commands.nextRoom();
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

	public void waitForPrompt() {
		commands.waitForPrompt();
	}

	public void waitForRoundtime() {
		commands.waitForRoundtime();
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
}
