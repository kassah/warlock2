package cc.warlock.rcp.ui.network;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.network.IConnection;
import cc.warlock.core.network.ILineConnectionListener;

public class SWTLineConnectionListenerAdapter extends SWTConnectionListenerAdapter implements ILineConnectionListener {

	private ILineConnectionListener lineListener;
	
	public SWTLineConnectionListenerAdapter (ILineConnectionListener listener) {
		super(listener);
		
		this.lineListener = listener;
	}
	
	private class LineReadyRunnable implements Runnable {
		public IConnection connection;
		public String line;
		
		public void run () {
			lineListener.lineReady(connection, line);
		}
	}
	
	public void lineReady(IConnection connection, String line) {
		LineReadyRunnable lineReadyRunnable = new LineReadyRunnable();
		lineReadyRunnable.connection = connection;
		lineReadyRunnable.line = line;
		Display.getDefault().asyncExec(lineReadyRunnable);		
	}
}
