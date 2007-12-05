package cc.warlock.core.stormfront.script.wsl;

abstract public class WSLAbstractCommand {
	private int lineNumber;
	
	public WSLAbstractCommand(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	abstract public void execute();
	
	public int getLineNumber() {
		return lineNumber;
	}
	

}
