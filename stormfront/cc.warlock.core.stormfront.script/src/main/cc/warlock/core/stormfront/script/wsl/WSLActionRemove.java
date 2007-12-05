package cc.warlock.core.stormfront.script.wsl;

public class WSLActionRemove extends WSLAbstractCommand {

	private WSLScript script;
	private IWSLValue when;
	
	public WSLActionRemove(int lineNum, WSLScript script, IWSLValue when) {
		super(lineNum);
		this.script = script;
		this.when = when;
	}
	
	@Override
	public void execute() {
		script.scriptCommands.removeAction(when.toString());
	}

}
