package cc.warlock.script.wsl;

public class WSLExistsCondition extends WSLBoolean {

	private String name;
	private WSLScript script;
	
	public WSLExistsCondition(String name, WSLScript script) {
		this.name = name;
		this.script = script;
	}
	
	public boolean getBoolean() {
		String value = script.getVariables().get(name);
		if(value == null) return false;
		else return true;
	}

}
