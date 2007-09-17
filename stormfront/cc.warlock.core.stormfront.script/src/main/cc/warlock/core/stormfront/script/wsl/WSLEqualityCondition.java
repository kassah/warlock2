package cc.warlock.core.stormfront.script.wsl;

import java.util.List;


public class WSLEqualityCondition extends WSLBoolean {

	public static enum EqualityOperator {
		equals { protected boolean eval(boolean value) { return value; } },
		notequals { protected boolean eval(boolean value) {return !value; } };
		
		protected abstract boolean eval(boolean eval);
		
		public boolean compare(IWSLValue arg1, IWSLValue arg2) {
			if(arg1.getType() == Type.Boolean || arg2.getType() == Type.Boolean) {
				return eval(arg1.getBoolean() == arg2.getBoolean());
			} else if(arg1.getType() == Type.Number || arg2.getType() == Type.Number) {
				return eval(arg1.getNumber() == arg2.getNumber());
			} else {
				return eval(arg1.getString().trim().equals(arg2.getString().trim()));
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
	
	public boolean getBoolean() {
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
