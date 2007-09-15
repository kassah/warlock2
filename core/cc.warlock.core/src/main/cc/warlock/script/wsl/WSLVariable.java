package cc.warlock.script.wsl;

public class WSLVariable extends WSLString {
	
	private String variableName;
	private WSLScript script;
	
	public WSLVariable(String var, WSLScript script) {
		variableName = var;
		this.script = script;
	}
	
	public String getString() {
		/*for(String name : variables.keySet()) {
			System.out.println("var: \"" + name + "\"");
		}
		System.out.println("variableName: \"" + variableName + "\"");*/
		
		String value = script.getVariables().get(variableName);
		if(value == null) return "";
		return value;
	}
}
