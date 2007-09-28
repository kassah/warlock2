package cc.warlock.core.stormfront.script.wsl;

import java.util.List;

public class WSLOrCondition extends WSLBoolean {

	private List<IWSLValue> args;
	
	public WSLOrCondition(List<IWSLValue> args) {
		this.args = args;
	}
	
	@Override
	public boolean toBoolean() {
		for(IWSLValue value : args) {
			if(value.toBoolean()) {
				return true;
			}
		}
		return false;
	}
}
