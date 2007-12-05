package cc.warlock.core.stormfront.script.wsl;

public class WSLString extends WSLAbstractString {
	
	private String string;
	
	public WSLString(String string) {
		this.string = string;
	}
	
	@Override
	public String toString() {
		return string;
	}
}
