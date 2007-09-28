package cc.warlock.core.stormfront.script.wsl;

import java.util.List;


public class WSLEqualityCondition extends WSLBoolean {

	public static enum EqualityOperator {
		equals { protected boolean eval(boolean value) { return value; } },
		notequals { protected boolean eval(boolean value) {return !value; } };
		
		protected abstract boolean eval(boolean eval);
		
		public boolean compare(IWSLValue arg1, IWSLValue arg2) {
			if(arg1.getType() == Type.Boolean || arg2.getType() == Type.Boolean) {
				return eval(arg1.toBoolean() == arg2.toBoolean());
			} else if(arg1.getType() == Type.Number || arg2.getType() == Type.Number) {
				return eval(arg1.toDouble() == arg2.toDouble());
			} else {
				return eval(arg1.toString().trim().equals(arg2.toString().trim()));
			}
		}
	}
	
	private List<IWSLValue> args;
	private List<EqualityOperator> equalityOps;
	
	public WSLEqualityCondition(List<IWSLValue> args,
			List<EqualityOperator> equalityOps) {
		this.args = args;
		this.equalityOps = equalityOps;
	}
	
	@Override
	public boolean toBoolean() {
		IWSLValue value = args.get(0);

		for(int i = 0; i < equalityOps.size(); i++) {
			IWSLValue nextValue = args.get(i + 1);
			if(!equalityOps.get(i).compare(value, nextValue))
				return false;
			value = nextValue;
		}
		return true;
	}
}
