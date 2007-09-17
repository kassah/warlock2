package cc.warlock.core.stormfront.script.wsl;

public class WSLNumber implements IWSLValue {

	private double value;
	
	public WSLNumber(double number) {
		value = number;
	}
	
	public WSLNumber(String number) {
		value = Double.parseDouble(number);
	}
	
	public boolean getBoolean() {
		if(value == 0.0) return false;
		else return true;
	}

	public double getNumber() {
		return value;
	}

	public String getString() {
		return Double.toString(value);
	}

	public Type getType() {
		return Type.Number;
	}

}
