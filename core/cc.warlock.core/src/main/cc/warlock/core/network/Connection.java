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
/*
 * Created on Sep 17, 2004
 */
package cc.warlock.core.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import cc.warlock.core.client.IWarlockClient;

/**
 * @author marshall
 */
public class Connection implements IConnection {

	protected Socket socket;
	protected ArrayList<IConnectionListener> connectionListeners;
	protected boolean connected;
	protected BufferedReader reader;
	protected String host = null;
	protected int port = -1;
	
	public Connection (String host, int port)
		throws IOException
	{
		this();
		connect (host, port);
	}
	
	public Connection ()
	{
		connected = false;
		connectionListeners = new ArrayList<IConnectionListener>();
	}
	
	public void connect (String host, int port)
		throws IOException
	{
		socket = new Socket(host, port);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		new Thread(new EventPollThread()).start();
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void disconnect()
		throws IOException
	{
		socket.shutdownOutput();
	}
	
	public void addConnectionListener (IConnectionListener listener)
	{
		connectionListeners.add(listener);
	}
	
	public void send (String toSend)
		throws IOException
	{
		send(toSend.getBytes());
	}
	
	public void send (byte[] bytes)
		throws IOException
	{
		socket.getOutputStream().write(bytes);
	}
	
	public void sendLine (String line)
		throws IOException
	{
		send (line + "\n");
	}
	
	public IWarlockClient getClient() {
		return null;
	}
	
	protected class EventPollThread implements Runnable
	{
		
		public void run ()
		{
			while (true)
			{
				if (!connected) {
					if (isSocketActive()) {
						connected = true;
						listenersConnected();
					}
				} else {
					if (!isSocketActive()) {
						connected = false;
						listenersDisconnected();
						break;
					} else {
						try {
							String line = reader.readLine();
							listenersGotLine(line);
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		private void listenersDisconnected ()
		{
			for (IConnectionListener listener : connectionListeners) {
				listener.disconnected(Connection.this);
			}
		}
		
		private void listenersConnected ()
		{
			for (IConnectionListener listener : connectionListeners) {
				listener.connected(Connection.this);
			}
		}
		
		private void listenersGotLine (String line)
		{
			for (IConnectionListener listener : connectionListeners) {
				listener.dataReady(Connection.this, line);
			}
		}
		
		private boolean isSocketActive() {
			if (!socket.isConnected()) return false;
			if (socket.isClosed()) return false;
			if (socket.isInputShutdown()) return false;
			if (socket.isOutputShutdown()) return false;
			
			return true; // If we reach here... we're most likely still connected
		}
	}
}