package cc.warlock.core.stormfront.script.wsl;

public interface IWSLValue {
	
	enum Type { Boolean, String, Number }
	
	public Type getType();
	public String toString();
	public boolean toBoolean();
	public double toDouble();
	
}
