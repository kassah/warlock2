package cc.warlock.core.stormfront.script.wsl;

abstract public class WSLAbstractNumber implements IWSLValue {
	
	public boolean toBoolean() {
		if(toDouble() == 0.0) return false;
		else return true;
	}

	abstract public double toDouble();
	
	public String toString() {
		double v = toDouble();
		if(Math.floor(v) == v) return Long.toString((long)v);
		return Double.toString(v);
	}

	public Type getType() {
		return Type.Number;
	}

}
