package cc.warlock.core.stormfront.script.wsl;

public class WSLBoolean implements IWSLValue {

	private boolean value;
	
	public WSLBoolean(boolean v) {
		value = v;
	}
	
	protected WSLBoolean() { }
	
	public boolean getBoolean() {
		return value;
	}

	public double getNumber() {
		if(getBoolean()) return 1.0;
		else return 0.0;
	}

	public String getString() {
		if(getBoolean()) return "true";
		else return "false";
	}

	public Type getType() {
		return Type.Boolean;
	}

}
