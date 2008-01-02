package cc.warlock.core.stormfront.script.wsl;

public class WSLCondition extends WSLAbstractCommand {

	private WSLScript script;
	private WSLAbstractCommand command;
	private IWSLValue condition;
	
	public WSLCondition(int lineNum, WSLScript script, IWSLValue condition, WSLAbstractCommand command) {
		super(lineNum);
		this.script = script;
		this.condition = condition;
		this.command = command;
	}
	
	public void execute() {
		boolean cond = condition.toBoolean();
		script.setLastCondition(cond);
		if(cond)
			command.execute();
	}

}
