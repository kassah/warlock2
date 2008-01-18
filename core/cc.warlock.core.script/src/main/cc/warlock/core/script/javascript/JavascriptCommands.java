package cc.warlock.core.script.javascript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;

import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.script.IScriptFileInfo;
import cc.warlock.core.script.internal.RegexMatch;
import cc.warlock.core.script.internal.TextMatch;

public class JavascriptCommands {

	protected IScriptCommands commands;
	protected JavascriptScript script;
	protected HashMap<Integer, TimerTask> timeTasks = new HashMap<Integer, TimerTask>();
	private int nextTimerID = 1;
	private Timer timer = new Timer();
	
	public JavascriptCommands(IScriptCommands commands, JavascriptScript script) {
		this.commands = commands;
		this.script = script;
	}

	public void echo(String text) {
		commands.echo(text);
	}

	public void include (String otherScript)
	{
		if (script.getScriptInfo() instanceof IScriptFileInfo)
		{
			IScriptFileInfo info = (IScriptFileInfo) script.getScriptInfo();
			
			File scriptFile = new File(otherScript);
			if (!scriptFile.exists())
			{
				scriptFile = new File(info.getScriptFile().getParentFile(), otherScript);
			}
			if (scriptFile.exists()) {
				try {
					FileReader reader = new FileReader(scriptFile);
					
					Script includedScript = 
						script.getContext().compileReader(reader, scriptFile.getName(), 1, null);
					
					includedScript.exec(script.getContext(), script.getScope());
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void move(String direction) {
		commands.move(direction);
	}

	public void pause(int seconds) {
		commands.pause(seconds);
	}

	public void put(String text) {
		commands.put(text);
	}
	
	public void waitFor(String string)
	{
		waitFor(new TextMatch(string, true));
	}
	
	// Default to case sensitivity
	public void waitForRe(String string) {
		waitForRe(string, false);
	}
	
	public void waitForRe(String string, Boolean ignoreCase)
	{
		waitFor(new RegexMatch(string, ignoreCase));
	}

	public void waitFor(IMatch match) {
		commands.waitFor(match);
	}

	public void waitForPrompt() {
		commands.waitForPrompt();
	}

	public void exit() throws Error {
		commands.stop();
		
		throw new Error();
	}
	
	public IScriptCommands getScriptCommands ()
	{
		return commands;
	}
	
	public JavascriptScript getScript ()
	{
		return script;
	}
	
	private class CommandCallback extends TimerTask {
		private String command;
		
		public CommandCallback(String command) {
			this.command = command;
		}
		
		public void run() {
			Context.enter();
			try {
				Script jsCommand = script.getContext().compileString(command, "callback", 0, null);

				jsCommand.exec(script.getContext(), script.getScope());
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				Context.exit();
			}
		}
	}
	
	public int setInterval(String command, long interval) {
		int id = nextTimerID++;
		
		CommandCallback c = new CommandCallback(command);
		timeTasks.put(id, c);
		timer.scheduleAtFixedRate(c, interval, interval);
		
		return id;
	}
	
	public int setTimeout(String command, long timeout) {
		int id = nextTimerID++;
		
		CommandCallback c = new CommandCallback(command);
		timeTasks.put(id, c);
		timer.schedule(c, timeout);
		
		return id;
	}
}
