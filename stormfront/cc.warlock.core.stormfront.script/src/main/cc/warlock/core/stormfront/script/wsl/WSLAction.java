package cc.warlock.core.stormfront.script.wsl;

public class WSLAction extends WSLAbstractCommand {

	private WSLScript script;
	private WSLAbstractCommand command;
	private IWSLValue when;
	
	public WSLAction(int lineNum, WSLScript script, WSLAbstractCommand command, IWSLValue when) {
		super(lineNum);
		this.script = script;
		this.command = command;
		this.when = when;
	}
	
	public void execute() {
		script.scriptCommands.addAction(command, when.toString());
	}

}
