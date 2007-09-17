package cc.warlock.core.stormfront.script.wsl;

import java.util.List;

public class WSLOrCondition extends WSLBoolean {

	private List<IWSLValue> args;
	
	public WSLOrCondition(List<IWSLValue> args) {
		this.args = args;
	}
	
	public boolean getBoolean() {
		for(IWSLValue value : args) {
			if(value.getBoolean()) {
				return true;
			}
		}
		return false;
	}
}
