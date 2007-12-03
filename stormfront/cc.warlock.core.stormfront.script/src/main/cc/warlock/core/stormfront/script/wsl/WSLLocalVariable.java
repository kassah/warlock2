package cc.warlock.core.stormfront.script.wsl;

public class WSLLocalVariable extends WSLAbstractVariable {
	
	private WSLScript script;
	
	public WSLLocalVariable(String var, WSLScript script) {
		super(var, "$");
		this.script = script;
	}
	
	@Override
	public IWSLValue getVariable() {
		return script.getLocalVariable(variableName);
	}
	
	@Override
	public boolean variableExists() {
		return script.localVariableExists(variableName);
	}
}
