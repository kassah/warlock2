package cc.warlock.core.stormfront.script.wsl;

public class WSLCondition extends WSLAbstractCommand {

	private WSLAbstractCommand command;
	private IWSLValue condition;
	
	public WSLCondition(int lineNum, IWSLValue condition, WSLAbstractCommand command) {
		super(lineNum);
		this.condition = condition;
		this.command = command;
	}
	
	@Override
	public void execute() {
		if(condition.toBoolean())
			command.execute();
	}

}
