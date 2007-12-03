package cc.warlock.core.stormfront.script.wsl;

public class WSLVariable extends WSLAbstractString {
	
	private String variableName;
	private WSLScript script;
	
	public WSLVariable(String var, WSLScript script) {
		variableName = var;
		this.script = script;
	}
	
	@Override
	public String toString() {
		IWSLValue value = script.getVariable(variableName);
		if(value == null) {
			return "%" + variableName;
		}
		return value.toString();
	}
}
