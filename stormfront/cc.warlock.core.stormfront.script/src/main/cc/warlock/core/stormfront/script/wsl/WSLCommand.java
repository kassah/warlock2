package cc.warlock.core.stormfront.script.wsl;

public class WSLCommand extends WSLAbstractCommand {

	private WSLScript script;
	private IWSLValue value;
	
	public WSLCommand(int lineNumber, WSLScript script, IWSLValue value) {
		super(lineNumber);
		this.script = script;
		this.value = value;
	}
	
	public void execute() {
		 // we can have empty commands at labels
		if(value == null)
			return;
		
		script.execute(value.toString());
	}
}
