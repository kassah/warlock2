/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.core.stormfront.script.wsl;

import java.util.List;


public class WSLEqualityCondition extends WSLAbstractBoolean {

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
