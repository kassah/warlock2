package cc.warlock.script.wsl;

import java.util.List;

public class WSLOrCondition implements IWSLValue {

	private List<IWSLValue> args;
	
	public WSLOrCondition(List<IWSLValue> args) {
		this.args = args;
	}
	
	public Type getType() {
		return Type.Boolean;
	}
	
	public boolean getBoolean() {
		boolean rv = false;
		for(IWSLValue value : args) {
			if(value.getBoolean()) {
				rv = true;
				break;
			}
		}
		return rv;
	}

	public String getString() {
		if(getBoolean()) return "true";
		else return "false";
	}

	public double getNumber() {
		if(getBoolean()) return 1.0;
		else return 0.0;
	}
}
