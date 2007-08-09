/*
 * Created on Feb 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.network;

import java.io.IOException;

import cc.warlock.client.IWarlockClient;

/**
 * @author sproctor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IConnection {
	
	public void connect(String host, int port) throws IOException;
	public void disconnect() throws IOException;
	public void addConnectionListener(IConnectionListener listener);
	public void send(String toSend) throws IOException;
	public void send(byte[] bytes) throws IOException;
	public void sendLine(String line) throws IOException;
	public IWarlockClient getClient();
	
}
