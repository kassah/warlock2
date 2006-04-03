/*
 * Created on Jan 13, 2005
 */
package com.arcaner.warlock.network;

import org.eclipse.swt.widgets.Display;

/**
 * @author Marshall
 */
public class SWTConnectionListenerAdapter implements IConnectionListener {

	private IConnectionListener toWrap;
	
	public SWTConnectionListenerAdapter (IConnectionListener toWrap)
	{
		this.toWrap = toWrap;
	}
	
	private class ConnectedRunnable implements Runnable
	{
		public Connection connection;
		
		public void run () {
			toWrap.connected(connection);
		}
	}
	
	public void connected(Connection connection) {
		ConnectedRunnable connectedRunnable = new ConnectedRunnable();
		connectedRunnable.connection = connection;
		Display.getDefault().asyncExec(connectedRunnable);
	}
	
	private class DataReadyRunnable implements Runnable
	{
		public Connection connection;
		public String line;
		
		public void run () {
			toWrap.dataReady(connection, line);
		}
	}
	
	public void dataReady(Connection connection, String line) {
		DataReadyRunnable dataReadyRunnable = new DataReadyRunnable();
		dataReadyRunnable.connection = connection;
		dataReadyRunnable.line = line;
		Display.getDefault().asyncExec(dataReadyRunnable);
	}

	private class DisconnectedRunnable implements Runnable
	{
		public Connection connection;
		
		public void run () {
			toWrap.disconnected(connection);
		}
	}
	
	public void disconnected(Connection connection) {
		DisconnectedRunnable disconnectedRunnable = new DisconnectedRunnable();
		disconnectedRunnable.connection = connection;
		Display.getDefault().asyncExec(disconnectedRunnable);
	}

}
