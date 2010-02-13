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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.ICommandHistory;
import cc.warlock.core.client.ICompass;
import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.logging.IClientLogger;
import cc.warlock.core.client.logging.LoggingConfiguration;
import cc.warlock.core.client.logging.SimpleLogger;
import cc.warlock.core.client.settings.WarlockClientPreferences;
import cc.warlock.core.network.IConnection;
import cc.warlock.core.util.Pair;


/**
 * @author Marshall
 */
public abstract class WarlockClient implements IWarlockClient {

	protected IConnection connection;
	protected IWarlockClientViewer viewer;
	protected ICommandHistory commandHistory = new CommandHistory();
	protected String streamPrefix;
	private Collection<IRoomListener> roomListeners = Collections.synchronizedCollection(new ArrayList<IRoomListener>());
	protected Property<ICompass> compass = new Property<ICompass>(null);
	protected IClientLogger logger;
	protected HashMap<String, IStream> streams = new HashMap<String, IStream>();
	protected ArrayList<Pair<String, IStreamListener>> potentialListeners = new ArrayList<Pair<String, IStreamListener>>();
	
	public WarlockClient () {
		streamPrefix = "client:" + hashCode() + ":";
		
		if (LoggingConfiguration.instance().getLogFormat().equals(LoggingConfiguration.LOG_FORMAT_TEXT))
		{
			logger = new SimpleLogger(this);
		}
		
		WarlockClientRegistry.addWarlockClientListener(new WarlockClientListener() {
			@Override
			public void clientDisconnected(IWarlockClient client) {
				if (client == WarlockClient.this && logger != null) {
					logger.flush();
				}
			}

			@Override
			public void clientActivated(IWarlockClient client) {}

			@Override
			public void clientConnected(IWarlockClient client) {}

			@Override
			public void clientRemoved(IWarlockClient client) {}

			@Override
			public void clientSettingsLoaded(IWarlockClient client) {}
		});
	}
	
	// IWarlockClient methods
	
	public ICommandHistory getCommandHistory() {
		return commandHistory;
	}
	
	public abstract void connect(String server, int port, String key) throws IOException;
	
	public void send(ICommand command) {
		getDefaultStream().sendCommand(command);
		
		try {
			if(connection != null)
				connection.send(command.getCommand());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setViewer(IWarlockClientViewer viewer) {
		this.viewer = viewer;
	}
	
	public IWarlockClientViewer getViewer() {
		return viewer;
	}
	
	public IStream getDefaultStream() {
		return getStream(IWarlockClient.DEFAULT_STREAM_NAME);
	}
	
	public IStream getStream(String streamName) {
		synchronized(streams) {
			return streams.get(streamName);
		}
	}
	
	public Collection<IStream> getStreams() {
		return streams.values();
	}
	
	public IConnection getConnection() {
		return connection;
	}
	
	public void flushStreams() {
		synchronized(streams) {
			for(IStream stream : streams.values()) {
				stream.flush();
			}
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
	
	public abstract WarlockClientPreferences getClientPreferences();

	public IClientLogger getLogger() {
		return logger;
	}
	
	public void playSound(InputStream stream) {
		viewer.playSound(stream);
	}
	
	public abstract IStream createStream(String streamName);
	
	public void addStreamListener(String streamName, IStreamListener listener) {
		IStream stream = streams.get(streamName);
		if(stream != null)
			stream.addStreamListener(listener);
		else
			potentialListeners.add(new Pair<String, IStreamListener>(streamName, listener));
	}
}
