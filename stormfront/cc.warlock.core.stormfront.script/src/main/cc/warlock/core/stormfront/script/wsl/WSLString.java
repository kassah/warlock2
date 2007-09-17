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
	
	public String getString() {
		return string;
	}

	public boolean getBoolean() {
		String str = getString().trim();
		
		if(str == null || str.equals("") || str.equals("false") || str.equals("0")) return false;
		else return true;
	}
	
	public double getNumber() {
		try {
			return Double.parseDouble(getString().trim());
		} catch(NumberFormatException e) {
			return 0.0;
		}
	}
}
