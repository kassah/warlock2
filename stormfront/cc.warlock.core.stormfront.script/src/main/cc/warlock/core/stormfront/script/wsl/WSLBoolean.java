package cc.warlock.core.stormfront.script.wsl;

public class WSLBoolean extends WSLAbstractBoolean {

	private boolean value;
	
	public WSLBoolean(boolean v) {
		value = v;
	}
	
	public boolean toBoolean() {
		return value;
	}

}
