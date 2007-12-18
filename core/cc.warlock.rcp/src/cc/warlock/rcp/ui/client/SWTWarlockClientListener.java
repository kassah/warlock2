package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;

public class SWTWarlockClientListener implements IWarlockClientListener {

	protected IWarlockClientListener listener;
	
	public SWTWarlockClientListener (IWarlockClientListener listener)
	{
		this.listener = listener;
	}
	
	private class ActivatedListener implements Runnable {
		private IWarlockClient client;

		public ActivatedListener(IWarlockClient client) {
			this.client = client;
		}
		
		public void run () {
			listener.clientActivated(client);
		}
	}
	
	private class ConnectedListener implements Runnable {
		private IWarlockClient client;
		
		public ConnectedListener(IWarlockClient client) {
			this.client = client;
		}
		
		public void run () {
			listener.clientConnected(client);
		}
	}
	
	private class DisconnectedListener implements Runnable {
		private IWarlockClient client;
		
		public DisconnectedListener(IWarlockClient client) {
			this.client = client;
		}
		
		public void run () {
			listener.clientDisconnected(client);
		}
	}
	
	private class RemovedListener implements Runnable {
		private IWarlockClient client;
		
		public RemovedListener(IWarlockClient client) {
			this.client = client;
		}
		
		public void run () {
			listener.clientConnected(client);
		}
	}
	
	public void clientActivated(IWarlockClient client) {
		Display.getDefault().asyncExec(new ActivatedListener(client));
	}

	public void clientConnected(IWarlockClient client) {
		Display.getDefault().asyncExec(new ConnectedListener(client));
	}

	public void clientDisconnected(IWarlockClient client) {
		Display.getDefault().asyncExec(new DisconnectedListener(client));
	}

	public void clientRemoved(IWarlockClient client) {
		Display.getDefault().asyncExec(new RemovedListener(client));
	}

}
