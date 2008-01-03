package cc.warlock.core.stormfront.script.wsl;

import java.util.HashMap;

import cc.warlock.core.stormfront.script.IStormFrontScriptCommand;

abstract public class WSLAbstractCommand implements IStormFrontScriptCommand {
	private int lineNumber;
	private HashMap<String, Object> properties  = new HashMap<String, Object>();
	
	public WSLAbstractCommand(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public Object getProperty(String name) {
		return properties.get(name);
	}
	
	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}
}
