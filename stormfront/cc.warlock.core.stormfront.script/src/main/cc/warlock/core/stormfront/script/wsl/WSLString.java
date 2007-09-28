package cc.warlock.core.stormfront.script.wsl;

public class WSLString implements IWSLValue {
	
	private String string;
	
	public WSLString(String string) {
		this.string = string;
	}
	
	protected WSLString() { }
	
	public Type getType() {
		return Type.String;
	}
	
	public String toString() {
		return string;
	}

	public boolean toBoolean() {
		String str = toString().trim();
		
		if(str == null || str.equals("") || str.equals("false") || str.equals("0")) return false;
		else return true;
	}
	
	public double toDouble() {
		try {
			return Double.parseDouble(toString().trim());
		} catch(NumberFormatException e) {
			return 0.0;
		}
	}
}
