package cc.warlock.core.stormfront.script.wsl;

abstract public class WSLAbstractVariable extends WSLAbstractString {
	
	protected String variableName;
	private String prefix;
	
	public WSLAbstractVariable(String var, String prefix) {
		variableName = var;
		this.prefix = prefix;
	}
	
	@Override
	public String toString() {
		IWSLValue value = getVariable();
		if(value == null) {
			return prefix + variableName;
		}
		return value.toString();
	}
	
	abstract protected IWSLValue getVariable();
	abstract protected boolean variableExists();
}
