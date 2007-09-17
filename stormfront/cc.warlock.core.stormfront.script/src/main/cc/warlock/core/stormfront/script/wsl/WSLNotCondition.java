package cc.warlock.core.stormfront.script.wsl;

public class WSLNotCondition extends WSLBoolean {

	private IWSLValue value;
	
	public WSLNotCondition(IWSLValue value) {
		this.value = value;
	}
	
	public boolean getBoolean() {
		return !value.getBoolean();
	}

}
