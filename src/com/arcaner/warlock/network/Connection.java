/*
 * Created on Sep 17, 2004
 */
package com.arcaner.warlock.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import com.arcaner.warlock.client.IWarlockClient;

/**
 * @author marshall
 */
public class Connection implements IConnection {

	protected Socket socket;
	protected ArrayList<IConnectionListener> connectionListeners;
	protected boolean connected;
	protected BufferedReader reader;
	
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
		System.out.print("SGE Out:");
		System.out.write(bytes); // Some Debug Output
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
							System.out.println("IO Exception in Run Thread");
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