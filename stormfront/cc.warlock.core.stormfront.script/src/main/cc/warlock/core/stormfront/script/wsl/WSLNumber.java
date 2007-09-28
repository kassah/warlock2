package cc.warlock.core.stormfront.script.wsl;

public class WSLNumber implements IWSLValue {

	private double value;
	
	public WSLNumber(double number) {
		value = number;
	}
	
	public WSLNumber(String number) {
		value = Double.parseDouble(number);
	}
	
	public boolean toBoolean() {
		if(toDouble() == 0.0) return false;
		else return true;
	}

	public double toDouble() {
		return value;
	}

	public String toString() {
		if(Math.floor(value) == value) return Long.toString((long)value);
		return Double.toString(toDouble());
	}

	public Type getType() {
		return Type.Number;
	}

}
