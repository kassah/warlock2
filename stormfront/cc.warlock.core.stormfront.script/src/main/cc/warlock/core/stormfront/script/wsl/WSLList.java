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

import java.util.ArrayList;
import java.util.List;

public class WSLList extends WSLAbstractString {

	private List<IWSLValue> list;
	
	public WSLList(List<IWSLValue> list) {
		this.list = list;
	}
	
	public WSLList(IWSLValue e) {
		list = new ArrayList<IWSLValue>();
		list.add(e);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		for(IWSLValue value : list) {
			buffer.append(value.toString());
		}
		
		return buffer.toString();
	}
	
	public void add(IWSLValue e) {
		list.add(e);
	}
	
	public void prepend(IWSLValue e) {
		list.add(0, e);
	}
}
