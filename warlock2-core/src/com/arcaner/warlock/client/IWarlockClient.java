/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.client;

import java.io.IOException;
import java.util.Collection;

/**
 * @author Marshall
 * 
 * This is the main interface that will be passed around for other API functions to send data to the game,
 * notify Warlock of events, and get metadata about the current state, etc.
 * 
 * Extension writers who wish to add support for their game should start by extending this interface (see IStormFrontClient)
 */
public interface IWarlockClient {
	
	public static final String DEFAULT_VIEW = "defaultView";
	
	/**
	 * Connect and handshake with the Simutronics server
	 * @param key
	 */
	public void connect(String server, int gamePort, String key) throws IOException;
	
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
	 * Appends text to the game window. Any text sent in from this method
	 * will have highlights, etc applied. Script commands will also be
	 * triggered from this method. 
	 * @param viewName Name of the view to output to
	 * @param text Text to append to the game window.
	 */
	public void append(String viewName, String text);
	
	/**
	 * Echo text to the warlock window.
	 * @param text The text to echo
	 * @param viewName The viewName to echo to.
	 */
	public void echo (String viewName, String text);
	
	/**
	 * Clear the view
	 * @param viewName View to clear
	 */
	public void clear(String viewName);
	
	/**
	 * Sets the title for this client. The viewer associated with this client will be responsible for handling this appropriately.
	 * @param title
	 */
	public void setTitle (String title);
	
	/**
	 * @return the list of viewers for this client.
	 */
	public Collection<IWarlockClientViewer> getViewers();
	
	/**
	 * Add a viewer to this client
	 * @param viewer The viewer to add
	 */
	public void addViewer (IWarlockClientViewer viewer);
	
	
}
