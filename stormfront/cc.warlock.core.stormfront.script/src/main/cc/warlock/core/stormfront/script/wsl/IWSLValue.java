package cc.warlock.core.stormfront.script.wsl;

public interface IWSLValue {
	
	enum Type { Boolean, String, Number }
	
	public Type getType();
	public String getString();
	public boolean getBoolean();
	public double getNumber();
}
