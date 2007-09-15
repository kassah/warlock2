package cc.warlock.script.wsl;

import java.util.List;


public class WSLEqualityCondition implements IWSLValue {

	static enum EqualityOperator {
		equals { boolean eval(boolean value) { return value; } },
		notequals { boolean eval(boolean value) {return !value; } };
		abstract boolean eval(boolean value);
	}
	
	private List<IWSLValue> args;
	private List<EqualityOperator> equalityOps;
	
	public WSLEqualityCondition(List<IWSLValue> args,
			List<EqualityOperator> equalityOps) {
		this.args = args;
		this.equalityOps = equalityOps;
	}
	
	public Type getType() {
		return Type.Boolean;
	}
	
	public boolean getBoolean() {
		IWSLValue value = args.get(0);

		for(int i = 0; i < equalityOps.size(); i++) {
			IWSLValue nextValue = args.get(i + 1);
			EqualityOperator op = equalityOps.get(i);
			if(value.getType() == Type.Boolean
					|| nextValue.getType() == Type.Boolean) {
				if(!op.eval(value.getBoolean() == nextValue.getBoolean()))
					return false;
			} else if(value.getType() == Type.Number
					|| nextValue.getType() == Type.Number) {
				if(!op.eval(value.getNumber() == nextValue.getNumber()))
					return false;
			} else {
				if(!op.eval(value.getString() == nextValue.getString()))
					return false;
			}
		}
		return true;
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
