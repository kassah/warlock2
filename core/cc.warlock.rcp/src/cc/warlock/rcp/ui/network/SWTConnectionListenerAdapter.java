/*
 * Created on Jan 13, 2005
 */
package cc.warlock.rcp.ui.network;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.network.IConnection;
import cc.warlock.core.network.IConnectionListener;

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
		public IConnection connection;
		
		public void run () {
			listener.connected(connection);
		}
	}
	
	public void connected(IConnection connection) {
		ConnectedRunnable connectedRunnable = new ConnectedRunnable();
		connectedRunnable.connection = connection;
		Display.getDefault().asyncExec(connectedRunnable);
	}
	
	private class DataReadyRunnable implements Runnable
	{
		public IConnection connection;
		public String line;
		
		public void run () {
			listener.dataReady(connection, line);
		}
	}
	
	public void dataReady(IConnection connection, String line) {
		DataReadyRunnable dataReadyRunnable = new DataReadyRunnable();
		dataReadyRunnable.connection = connection;
		dataReadyRunnable.line = line;
		Display.getDefault().asyncExec(dataReadyRunnable);
	}

	private class DisconnectedRunnable implements Runnable
	{
		public IConnection connection;
		
		public void run () {
			listener.disconnected(connection);
		}
	}
	
	public void disconnected(IConnection connection) {
		DisconnectedRunnable disconnectedRunnable = new DisconnectedRunnable();
		disconnectedRunnable.connection = connection;
		Display.getDefault().asyncExec(disconnectedRunnable);
	}

}
