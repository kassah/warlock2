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
package cc.warlock.core.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The warlock client registry is a single place of entry for finding and discovering about IWarlockClients that exist, or registering as a listener for
 * clients that will be created/destroyed
 * 
 * @author Marshall
 */
public class WarlockClientRegistry {

	protected static ArrayList<IWarlockClient> clients = new ArrayList<IWarlockClient>();
	protected static ArrayList<IWarlockClientListener> listeners = new ArrayList<IWarlockClientListener>();
	
	public static void addWarlockClientListener (IWarlockClientListener listener)
	{
		listeners.add(listener);
	}
	
	public static void removeWarlockClientListener (IWarlockClientListener listener)
	{
		if (listeners.contains(listener))
			listeners.remove(listener);
	}
	
	public static void activateClient (IWarlockClient client)
	{
		if (!clients.contains(client))
		{
			clients.add(client);
		}
		
		for (IWarlockClientListener listener : listeners)
			listener.clientActivated(client);
	}
	
	public static void clientConnected (IWarlockClient client)
	{
		for (IWarlockClientListener listener : listeners)
			listener.clientConnected(client);
	}
	
	public static void clientDisconnected (IWarlockClient client)
	{
		for (IWarlockClientListener listener : listeners)
			listener.clientDisconnected(client);
	}
	
	public static void removeClient (IWarlockClient client)
	{
		if (clients.contains(client))
			clients.remove(client);
		
		for (IWarlockClientListener listener : listeners)
			listener.clientRemoved(client);
	}
	
	public static void clientSettingsLoaded (IWarlockClient client)
	{
		for (IWarlockClientListener listener : listeners)
			listener.clientSettingsLoaded(client);
	}
	
	public static List<IWarlockClient> getActiveClients ()
	{
		return Collections.unmodifiableList(clients);
	}
	
	public static List<IWarlockClient> getActiveClientsOrListen (IWarlockClientListener listener)
	{
		if (clients.size() > 0)
		{
			return getActiveClients();
		}
		else {
			addWarlockClientListener(listener);
			return Collections.emptyList();
		}
	}
}
