package cc.warlock.core.stormfront.script.wsl;

abstract public class WSLAbstractString implements IWSLValue {
	
	public Type getType() {
		return Type.String;
	}
	
	abstract public String toString();

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
