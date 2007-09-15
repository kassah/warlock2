package cc.warlock.script.wsl;

public class WSLString implements IWSLValue {
	
	private String string;
	
	public WSLString(String string) {
		this.string = string;
	}
	
	public Type getType() {
		return Type.String;
	}
	
	public String getString() {
		return string;
	}

	public boolean getBoolean() {
		if(string == null || string.equals("") || string.equals("false") || string.equals("0")) return false;
		else return true;
	}
	
	public double getNumber() {
		try {
			return Integer.parseInt(string);
		} catch(NumberFormatException e) {
			return 0.0;
		}
	}
}
