package cc.warlock.script.wsl;

import java.util.List;

public class WSLRelationalCondition extends WSLBoolean {

	public enum RelationalOperator {
		GreaterThan			{ boolean compare(IWSLValue arg1, IWSLValue arg2) { return arg1.getNumber() > arg2.getNumber(); } },
		GreaterThanEqualTo	{ boolean compare(IWSLValue arg1, IWSLValue arg2) { return arg1.getNumber() >= arg2.getNumber(); } },
		LessThan			{ boolean compare(IWSLValue arg1, IWSLValue arg2) { return arg1.getNumber() < arg2.getNumber(); } },
		LessThanEqualTo		{ boolean compare(IWSLValue arg1, IWSLValue arg2) { return arg1.getNumber() <= arg2.getNumber(); } };
	
		abstract boolean compare(IWSLValue arg1, IWSLValue arg2);
	}
	
	private List<IWSLValue> args;
	private List<RelationalOperator> ops;
	
	public WSLRelationalCondition(List<IWSLValue> args, List<RelationalOperator> ops) {
		this.args = args;
		this.ops = ops;
	}
	
	public boolean getBoolean() {
		IWSLValue value = args.get(0);
		for(int i = 0; i < ops.size(); i++) {
			IWSLValue nextValue = args.get(i + 1);
			if(!ops.get(i).compare(value, nextValue))
				return false;
			value = nextValue;
		}
		return true;
	}
}
