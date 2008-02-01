/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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
public interface IWarlockClient extends IRoomListener {
	
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
	public void send(String command);
	
	/**
	 * Send command to the game.
	 * @param prefix to prepend when we echo the command
	 * @param command The command to send.
	 */
	public void send(String prefix, String command);
	
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
	 * Remove a viewer from this client
	 * @param viewer The viewer to remove
	 */
	public void removeViewer (IWarlockClientViewer viewer);

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
	
	/**
	 * @return The style for commands output by this client
	 */
	public IWarlockStyle getCommandStyle();
	
	/**
	 * add a highlight provider
	 * @param highlightProvider provider to add
	 */
	public void addHighlightProvider(IHighlightProvider highlightProvider);
	
	/**
	 * remove a highlight provider
	 * @param highlightProvider
	 */
	public void removeHighlightProvider(IHighlightProvider highlightProvider);
	
	/**
	 * @return combination of highlight strings from all of the highlight providers
	 */
	public Collection<IHighlightString> getHighlightStrings();
	
	public void flushStreams();
	
	/**
	 * @return a list of streams for this client
	 */
	public Collection<IStream> getStreams();
	
	public void addRoomListener(IRoomListener roomListener);
	public void removeRoomListener(IRoomListener roomListener);
	
	/**
	 * @return The client's compass.
	 */
	public IProperty<ICompass> getCompass();
	
}
