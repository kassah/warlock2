/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.client;

import java.io.IOException;
import java.util.Collection;

import cc.warlock.core.network.IConnection;



/**
 * @author Marshall
 * 
 * This is the main interface that will be passed around for other API functions to send data to the game,
 * notify Warlock of events, and get metadata about the current state, etc.
 * 
 * Extension writers who wish to add support for their game should start by extending this interface (see IStormFrontClient)
 */
public interface IWarlockClient {
	
	public static final String DEFAULT_STREAM_NAME = "defaultView";
	
	/**
	 * Connect and handshake with the Simutronics server
	 * @param key
	 */
	public void connect(String server, int gamePort, String key) throws IOException;
	
	/**
	 * Send command to the game.
	 * @param command The command to send.
	 */
	public void send (ICommand command);
	
	/**
	 * Send command to the game.
	 * @param command The command to send.
	 */
	public void send(String command);
	
	/**
	 * @return This client's command history
	 */
	public ICommandHistory getCommandHistory();
	
	/**
	 * @return the list of viewers for this client.
	 */
	public Collection<IWarlockClientViewer> getViewers();
	
	/**
	 * Add a viewer to this client
	 * @param viewer The viewer to add
	 */
	public void addViewer (IWarlockClientViewer viewer);

	/**
	 * Functionally equivalent to getStream(DEFAULT_STREAM_NAME)
	 * @return The default stream to send data to.
	 */
	public IStream getDefaultStream();
	
	/**
	 * @param streamName The stream name
	 * @return The stream associated with the given name. If this stream does not exist, it will be lazily created.
	 */
	public IStream getStream(String streamName);
	
	/**
	 * Get the current buffer for the passed-in stream name
	 * @param stream
	 * @return
	 */
	public IStyledString getStreamBuffer(IStream stream);
	
	/**
	 * Get the connection associated with this client.
	 * @@WARNING@@ 
	 * Do not use the raw connection unless you know what you are doing!
	 * @return
	 */
	public IConnection getConnection ();
	
	/**
	 * @return The skin for this client
	 */
	public IWarlockSkin getSkin();
}