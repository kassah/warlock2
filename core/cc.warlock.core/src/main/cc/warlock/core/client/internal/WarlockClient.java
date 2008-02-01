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
package cc.warlock.core.client.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import cc.warlock.core.client.ICommandHistory;
import cc.warlock.core.client.ICompass;
import cc.warlock.core.client.IHighlightProvider;
import cc.warlock.core.client.IHighlightString;
import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.network.IConnection;


/**
 * @author Marshall
 */
public abstract class WarlockClient implements IWarlockClient {

	protected IConnection connection;
	protected ArrayList<IWarlockClientViewer> viewers;
	protected ICommandHistory commandHistory = new CommandHistory();
	protected String streamPrefix;
	protected HashSet<IHighlightProvider> highlightProviders = new HashSet<IHighlightProvider>();
	private Collection<IRoomListener> roomListeners = Collections.synchronizedCollection(new ArrayList<IRoomListener>());
	protected ClientProperty<ICompass> compass = new ClientProperty<ICompass>(this, "compass", null);
	
	public WarlockClient () {
		viewers = new ArrayList<IWarlockClientViewer>();
		streamPrefix = "client:" + hashCode() + ":";
	}
	
	// IWarlockClient methods
	
	public ICommandHistory getCommandHistory() {
		return commandHistory;
	}
	
	public abstract void connect(String server, int port, String key) throws IOException;
	
	public void send(String command) {
		send(null, command);
	}
	
	public void send(String prefix, String command) {
		if(connection == null) {
			// Not yet connected to server
			return;
		}
		
		String text = command + "\n";
		if(prefix != null)
			getDefaultStream().sendCommand(prefix + text);
		else
			getDefaultStream().sendCommand(text);
		
		try {
			connection.send(text);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public Collection<IWarlockClientViewer> getViewers() {
		return viewers;
	}
	
	public void addViewer(IWarlockClientViewer viewer) {
		viewers.add(viewer);
	}
	
	public void removeViewer(IWarlockClientViewer viewer) {
		if (viewers.contains(viewer))
			viewers.remove(viewer);
	}
	
	public IStream getDefaultStream() {
		return getStream(IWarlockClient.DEFAULT_STREAM_NAME);
	}
	
	public IStream getStream(String streamName) {
		return Stream.fromName(this, streamPrefix + streamName);
	}
	
	public IConnection getConnection() {
		return connection;
	}
	
	public void addHighlightProvider(IHighlightProvider highlightProvider) {
		highlightProviders.add(highlightProvider);
	}
	
	public void removeHighlightProvider(IHighlightProvider highlightProvider) {
		highlightProviders.remove(highlightProvider);
	}
	
	public Collection<IHighlightString> getHighlightStrings() {
		ArrayList<IHighlightString> strings = new ArrayList<IHighlightString>();
		
		for(IHighlightProvider highlightProvider : highlightProviders) {
			strings.addAll(highlightProvider.getHighlightStrings());
		}
		
		return strings;
	}
	
	public Collection<IStream> getStreams() {
		Collection<IStream> streams = new ArrayList<IStream>();
		for(IStream stream : Stream.getStreams()) {
			IWarlockClient client = stream.getClient();
			if (client != null && client.equals(this))
				streams.add(stream);
		}
		return streams;
	}
	
	public void flushStreams() {
		for(IStream stream : getStreams()) {
			stream.flush();
		}
	}
	
	public void addRoomListener(IRoomListener roomListener) {
		synchronized(roomListeners) {
			roomListeners.add(roomListener);
		}
	}
	
	public void removeRoomListener(IRoomListener roomListener) {
		synchronized(roomListeners) {
			roomListeners.remove(roomListener);
		}
	}
	
	public void nextRoom() {
		synchronized(roomListeners) {
			for(IRoomListener listener : roomListeners)
				listener.nextRoom();
		}
	}
	
	public IProperty<ICompass> getCompass() {
		return compass;
	}
	
}
