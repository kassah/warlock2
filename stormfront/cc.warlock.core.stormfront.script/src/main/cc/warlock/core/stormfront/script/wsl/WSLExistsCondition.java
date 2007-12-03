package cc.warlock.core.stormfront.script.wsl;

public class WSLExistsCondition extends WSLAbstractBoolean {

	private String name;
	private WSLScript script;
	
	public WSLExistsCondition(String name, WSLScript script) {
		this.name = name;
		this.script = script;
	}
	
	@Override
	public boolean toBoolean() {
		return script.variableExists(name);
	}

}
