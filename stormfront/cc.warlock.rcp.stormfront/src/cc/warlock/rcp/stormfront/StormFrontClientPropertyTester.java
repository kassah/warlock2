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
package cc.warlock.rcp.stormfront;

import org.eclipse.core.expressions.PropertyTester;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.rcp.ui.client.WarlockClientAdaptable;

public class StormFrontClientPropertyTester extends PropertyTester {

	public StormFrontClientPropertyTester() {
		// TODO Auto-generated constructor stub
	}

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		WarlockClientAdaptable adaptable = (WarlockClientAdaptable) receiver;
		IWarlockClient client = (IWarlockClient) adaptable.getAdapter(IWarlockClient.class);
		
		if ("clientConnected".equals(property))
		{
			boolean connected = client.getConnection() == null ? false : client.getConnection().isConnected();
			
			return expectedValue == null ?
				connected : connected == ((Boolean)expectedValue).booleanValue();
		}
		else if ("settingsLoaded".equals(property))
		{
		}
		
		return false;
	}

}
