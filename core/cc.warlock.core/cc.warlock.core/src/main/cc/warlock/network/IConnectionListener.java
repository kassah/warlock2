/*
 * Created on Sep 17, 2004
 */
package cc.warlock.network;


/**
 * @author marshall
 */
public interface IConnectionListener {

	public void connected (IConnection connection);
	public void dataReady (IConnection connection, String line);
	public void disconnected (IConnection connection);
	
}
