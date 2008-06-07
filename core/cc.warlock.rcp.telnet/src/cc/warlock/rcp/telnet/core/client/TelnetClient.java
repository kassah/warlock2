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
package cc.warlock.rcp.telnet.core.client;

import java.io.IOException;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IWarlockSkin;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.internal.Property;
import cc.warlock.core.client.internal.WarlockClient;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.network.Connection;
import cc.warlock.rcp.telnet.ui.DefaultSkin;

/**
 * @author Will Robertson
 *
 */
public class TelnetClient extends WarlockClient {
	protected String hostname;

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.internal.WarlockClient#connect(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void connect(String server, int port, String key) throws IOException {
		// TODO Auto-generated method stub
		hostname = server;
		connection = new Connection(server, port);
		connection.connect(server, port);
		
		WarlockClientRegistry.clientConnected(this);
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockClient#getCharacterName()
	 */
	public IProperty<String> getCharacterName() {
		return new Property<String>("charName","Telnet");
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockClient#getClientId()
	 */
	public IProperty<String> getClientId() {
		// TODO Auto-generated method stub
		return new Property<String>("ClientId","telnet."+hostname);
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockClient#getCommandStyle()
	 */
	public IWarlockStyle getCommandStyle() {
		// TODO Auto-generated method stub
		return new WarlockStyle();
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.client.IWarlockClient#getSkin()
	 */
	public IWarlockSkin getSkin() {
		// TODO Auto-generated method stub
		return new DefaultSkin();
	}

}
