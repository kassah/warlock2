package cc.warlock.core.stormfront.script.wsl;

import java.util.List;

public class WSLAndCondition extends WSLAbstractBoolean {

	private List<IWSLValue> args;
	
	public WSLAndCondition(List<IWSLValue> args) {
		this.args = args;
	}
	
	@Override
	public boolean toBoolean() {
		for(IWSLValue value : args) {
			if(!value.toBoolean()) {
				return false;
			}
		}
		return true;
	}
}
