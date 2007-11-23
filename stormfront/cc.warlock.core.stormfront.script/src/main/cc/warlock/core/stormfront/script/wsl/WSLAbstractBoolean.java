package cc.warlock.core.stormfront.script.wsl;

abstract public class WSLAbstractBoolean implements IWSLValue {
	
	abstract public boolean toBoolean();

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
