package cc.warlock.core.stormfront.script.wsl;

import cc.warlock.core.script.internal.RegexMatch;

public class WSLAction extends WSLAbstractCommand {

	private WSLScript script;
	private WSLAbstractCommand command;
	private IWSLValue when;
	private RegexMatch match;
	
	public WSLAction(int lineNum, WSLScript script, WSLAbstractCommand command, IWSLValue when) {
		super(lineNum);
		this.script = script;
		this.command = command;
		this.when = when;
	}
	
	private class WSLActionAdapter implements Runnable {
		
		public void run() {
			script.setVariablesFromMatch((RegexMatch)match);
			command.execute();
		}
	}
	
	public void execute() {
		match = new RegexMatch(when.toString().trim());
		script.scriptCommands.addAction(new WSLActionAdapter(), match);
	}

}
