package cc.warlock.core.script.javascript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import org.mozilla.javascript.Script;

import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.script.IScriptFileInfo;
import cc.warlock.core.script.internal.RegexMatch;
import cc.warlock.core.script.internal.TextMatch;

public class JavascriptCommands {

	protected IScriptCommands commands;
	protected JavascriptScript script;
	
	public class JavascriptStopException extends Exception implements Serializable {
		private static final long serialVersionUID = 7226391328268718796L;
	}
	
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

	public void exit() throws JavascriptStopException {
		script.stop();
		
		throw new JavascriptStopException();
	}
	
	public void stop() {
		commands.stop();
	}
	
	public IScriptCommands getScriptCommands ()
	{
		return commands;
	}
	
	public JavascriptScript getScript ()
	{
		return script;
	}
}
