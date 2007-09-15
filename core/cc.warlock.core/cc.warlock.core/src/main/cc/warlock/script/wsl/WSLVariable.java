package cc.warlock.script.wsl;

public class WSLVariable implements IWSLValue {
	
	private String variableName;
	private WSLScript script;
	
	public WSLVariable(String var, WSLScript script) {
		variableName = var;
		this.script = script;
	}
	
	public Type getType() {
		return Type.String;
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

	public boolean getBoolean() {
		String text = getString();
		if(text.equals("") || text.equals("0") || text.equals("false")) return false;
		else return true;
	}
	
	public double getNumber() {
		String text = getString();
		try {
			return Integer.parseInt(text);
		} catch(NumberFormatException e) {
			return 0.0;
		}
	}
}
