package cc.warlock.core.client.internal;

public class WarlockMacro {
	private int keycode;
	private int modifiers;
	private String command;
	
	public WarlockMacro(int keycode, int modifiers, String command) {
		this.keycode = keycode;
		this.modifiers = modifiers;
		this.command = command;
	}
	
	public int getKeycode() {
		return keycode;
	}
	
	public int getModifiers() {
		return modifiers;
	}
	
	public String getCommand() {
		return command;
	}
	
	public void setKeycode(int keycode) {
		this.keycode = keycode;
	}
	
	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
}
