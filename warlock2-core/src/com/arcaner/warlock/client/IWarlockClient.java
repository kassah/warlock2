/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.client;

import java.io.IOException;

/**
 * @author Marshall
 * 
 * This is the main interface that will be passed around for other API functions to send data to the game,
 * notify Warlock of events, and get metadata about the current state, etc.
 * 
 * Extension writers who wish to add support for their game should start by extending this interface (see IStormFrontClient)
 */
public interface IWarlockClient {
	
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
	public void output(String viewName, String text);
	
	/**
	 * Echo text to the warlock window.
	 * @param text The text to echo
	 */
	public void echo (String text);
	
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
	
}
