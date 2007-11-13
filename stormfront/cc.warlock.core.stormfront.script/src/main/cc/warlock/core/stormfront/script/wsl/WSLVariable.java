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
		IWSLValue value = script.getVariable(variableName);
		if(value == null) {
			if(variableName.startsWith("$"))
				return variableName;
			return "%" + variableName;
		}
		return value.toString();
	}
}
