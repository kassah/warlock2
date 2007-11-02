package cc.warlock.core.stormfront.script.wsl;

public class WSLVariable extends WSLString {
	
	private String variableName;
	private WSLScript script;
	
	public WSLVariable(String var, WSLScript script) {
		variableName = var;
		this.script = script;
	}
	
	@Override
	public String toString() {
		String value = script.getVariable(variableName).toString();
		if(value == null) return "";
		return value;
	}
}
