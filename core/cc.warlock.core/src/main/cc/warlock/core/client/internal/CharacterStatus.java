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
package cc.warlock.core.client.internal;

import java.util.HashMap;
import java.util.Map;

import cc.warlock.core.client.ICharacterStatus;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.IWarlockClient;

public class CharacterStatus extends Property<String> implements ICharacterStatus {
	
	protected HashMap<StatusType, Boolean> status = new HashMap<StatusType, Boolean>();
	
	public CharacterStatus (IWarlockClient client)
	{
		super("characterStatus", null);
		
		for (StatusType type : StatusType.values())
		{
			status.put(type, false);
		}
	}
	
	@Override
	public void clear() {
		for (StatusType type : StatusType.values())
		{
			status.put(type, false);
		}
		
		for (IPropertyListener<String> listener : listeners) {
			listener.propertyCleared();
		}
	}
	
	public void set(String data) {
		StatusType statusType = StatusType.getStatusType(data);
		
		if (statusType != null)
		{
			status.put(statusType, true);
			for (IPropertyListener<String> listener : listeners)
			{
				listener.propertyChanged(this.get());
			}
		}
	}
	
	public void unset(String data) {
		StatusType statusType = StatusType.getStatusType(data);
		
		if (statusType != null)
		{
			status.put(statusType, false);
			for (IPropertyListener<String> listener : listeners)
			{
				listener.propertyChanged(this.get());
			}
		}
	}
	
	public Map<StatusType, Boolean> getStatus() {
		return status;
	}
}
