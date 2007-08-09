/*
 * Created on Sep 17, 2004
 */
package cc.warlock.network;


/**
 * @author marshall
 */
public interface IConnectionListener {

	public void connected (Connection connection);
	public void dataReady (Connection connection, String line);
	public void disconnected (Connection connection);
	
}
