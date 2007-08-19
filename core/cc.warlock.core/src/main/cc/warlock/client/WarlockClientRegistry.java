package cc.warlock.client;

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
	
	public static List<IWarlockClient> getActiveClients ()
	{
		return Collections.unmodifiableList(clients);
	}
}
