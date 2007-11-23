package cc.warlock.core.stormfront.script.wsl;

public class WSLNotCondition extends WSLAbstractBoolean {

	private IWSLValue value;
	
	public WSLNotCondition(IWSLValue value) {
		this.value = value;
	}
	
	@Override
	public boolean toBoolean() {
		return !value.toBoolean();
	}

}
