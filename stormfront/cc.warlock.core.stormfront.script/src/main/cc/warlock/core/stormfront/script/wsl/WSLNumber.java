package cc.warlock.core.stormfront.script.wsl;

public class WSLNumber extends WSLAbstractNumber {

	private double value;
	
	public WSLNumber(double number) {
		value = number;
	}
	
	public WSLNumber(String number) {
		value = Double.parseDouble(number);
	}
	
	public double toDouble() {
		return value;
	}

}
