package cc.warlock.core.stormfront.script.wsl;

public class WSLLocalVariable extends WSLAbstractString {
	
	private String variableName;
	private WSLScript script;
	
	public WSLLocalVariable(String var, WSLScript script) {
		variableName = var;
		this.script = script;
	}
	
	@Override
	public String toString() {
		IWSLValue value = script.getLocalVariable(variableName);
		if(value == null) {
			return "$" + variableName;
		}
		return value.toString();
	}
}
