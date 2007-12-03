package cc.warlock.core.stormfront.script.wsl;

public class WSLVariable extends WSLAbstractVariable {
	
	private WSLScript script;
	
	public WSLVariable(String var, WSLScript script) {
		super(var, "%");
		this.script = script;
	}
	
	@Override
	public IWSLValue getVariable() {
		return script.getVariable(variableName);
	}
	
	@Override
	public boolean variableExists() {
		return script.variableExists(variableName);
	}
}
