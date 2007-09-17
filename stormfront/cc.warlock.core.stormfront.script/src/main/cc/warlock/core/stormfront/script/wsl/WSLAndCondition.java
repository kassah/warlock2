package cc.warlock.core.stormfront.script.wsl;

import java.util.List;

public class WSLAndCondition extends WSLBoolean {

	private List<IWSLValue> args;
	
	public WSLAndCondition(List<IWSLValue> args) {
		this.args = args;
	}
	
	public boolean getBoolean() {
		for(IWSLValue value : args) {
			if(!value.getBoolean()) {
				return false;
			}
		}
		return true;
	}
}
