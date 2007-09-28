package cc.warlock.core.stormfront.script.wsl;

import java.util.List;

public class WSLRelationalCondition extends WSLBoolean {

	public enum RelationalOperator {
		GreaterThan			{ boolean compare(IWSLValue arg1, IWSLValue arg2) { return arg1.toDouble() > arg2.toDouble(); } },
		GreaterThanEqualTo	{ boolean compare(IWSLValue arg1, IWSLValue arg2) { return arg1.toDouble() >= arg2.toDouble(); } },
		LessThan			{ boolean compare(IWSLValue arg1, IWSLValue arg2) { return arg1.toDouble() < arg2.toDouble(); } },
		LessThanEqualTo		{ boolean compare(IWSLValue arg1, IWSLValue arg2) { return arg1.toDouble() <= arg2.toDouble(); } };
	
		abstract boolean compare(IWSLValue arg1, IWSLValue arg2);
	}
	
	private List<IWSLValue> args;
	private List<RelationalOperator> ops;
	
	public WSLRelationalCondition(List<IWSLValue> args, List<RelationalOperator> ops) {
		this.args = args;
		this.ops = ops;
	}
	
	@Override
	public boolean toBoolean() {
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
