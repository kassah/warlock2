package cc.warlock.core.stormfront.script.wsl;

public class WSLActionClear extends WSLAbstractCommand {

	private WSLScript script;
	
	public WSLActionClear(int lineNum, WSLScript script) {
		super(lineNum);
		this.script = script;
	}
	
	public void execute() {
		script.scriptCommands.clearActions();
	}

}
