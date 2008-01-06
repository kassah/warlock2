package cc.warlock.core.stormfront.script.wsl;


abstract public class WSLAbstractCommand {
	private int lineNumber;
	
	public WSLAbstractCommand(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	abstract public void execute();

}
