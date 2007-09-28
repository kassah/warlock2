package cc.warlock.core.stormfront.script.wsl;

public class WSLBoolean implements IWSLValue {

	private boolean value;
	
	public WSLBoolean(boolean v) {
		value = v;
	}
	
	protected WSLBoolean() { }
	
	public boolean toBoolean() {
		return value;
	}

	public double toDouble() {
		if(toBoolean()) return 1.0;
		else return 0.0;
	}

	public String toString() {
		if(toBoolean()) return "true";
		else return "false";
	}

	public Type getType() {
		return Type.Boolean;
	}

}
