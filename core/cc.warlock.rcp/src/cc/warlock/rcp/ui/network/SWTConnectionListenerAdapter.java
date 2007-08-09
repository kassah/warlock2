/*
 * Created on Jan 13, 2005
 */
package cc.warlock.rcp.ui.network;

import org.eclipse.swt.widgets.Display;

import cc.warlock.network.Connection;
import cc.warlock.network.IConnectionListener;

/**
 * @author Marshall
 */
public class SWTConnectionListenerAdapter implements IConnectionListener {

	private IConnectionListener listener;
	
	public SWTConnectionListenerAdapter (IConnectionListener listener)
	{
		this.listener = listener;
	}
	
	private class ConnectedRunnable implements Runnable
	{
		public Connection connection;
		
		public void run () {
			listener.connected(connection);
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
			listener.dataReady(connection, line);
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
			listener.disconnected(connection);
		}
	}
	
	public void disconnected(Connection connection) {
		DisconnectedRunnable disconnectedRunnable = new DisconnectedRunnable();
		disconnectedRunnable.connection = connection;
		Display.getDefault().asyncExec(disconnectedRunnable);
	}

}
