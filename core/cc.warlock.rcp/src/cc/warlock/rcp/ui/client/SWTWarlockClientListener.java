package cc.warlock.rcp.ui.client;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;

public class SWTWarlockClientListener implements IWarlockClientListener {

	protected IWarlockClientListener listener;
	protected ListenerWrapper wrapper = new ListenerWrapper();
	
	public SWTWarlockClientListener (IWarlockClientListener listener)
	{
		this.listener = listener;
	}
	
	private static enum EventType {Activated, Connected, Disconnected, Removed}
	
	private class ListenerWrapper implements Runnable {
		public IWarlockClient client;
		public EventType eventType;
		
		public void run () {
			switch (eventType) {
			case Activated: listener.clientActivated(client); break;
			case Connected: listener.clientConnected(client); break;
			case Disconnected: listener.clientDisconnected(client); break;
			case Removed: listener.clientRemoved(client); break;
			}
		}
	}
	
	public void clientActivated(IWarlockClient client) {
		wrapper.client = client;
		wrapper.eventType = EventType.Activated;
		Display.getDefault().syncExec(wrapper);
	}

	public void clientConnected(IWarlockClient client) {
		wrapper.client = client;
		wrapper.eventType = EventType.Connected;
		Display.getDefault().syncExec(wrapper);
	}

	public void clientDisconnected(IWarlockClient client) {
		wrapper.client = client;
		wrapper.eventType = EventType.Disconnected;
		Display.getDefault().syncExec(wrapper);
	}

	public void clientRemoved(IWarlockClient client) {
		wrapper.client = client;
		wrapper.eventType = EventType.Removed;
		Display.getDefault().syncExec(wrapper);
	}

}
