package cc.warlock.core.stormfront.script.wsl;

import cc.warlock.core.stormfront.script.IStormFrontScriptCommand;

abstract public class WSLAbstractCommand implements IStormFrontScriptCommand {
	private int lineNumber;
	
	public WSLAbstractCommand(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
}
