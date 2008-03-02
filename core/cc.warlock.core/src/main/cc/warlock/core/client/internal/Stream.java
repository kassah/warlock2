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
 * Created on Jan 16, 2005
 */
package cc.warlock.core.client.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockString;


/**
 * @author Marshall
 * 
 * The internal implementation of a StormFront stream.
 */
public class Stream implements IStream {
	
	protected static Hashtable<String, Stream> streams = new Hashtable<String, Stream>();
	protected IWarlockClient client;
	
	protected IProperty<String> streamName, streamTitle;
	private ArrayList<IStreamListener> listeners = new ArrayList<IStreamListener>();
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private Lock readLock = lock.readLock();
	private Lock writeLock = lock.writeLock();
	protected boolean isPrompting = false;
	private boolean hasView = false;
	protected boolean isLogging = false;
	
	protected Stream (IWarlockClient client, String streamName) {
		this.client = client;
		this.streamName = new Property<String>("streamName", null);
		this.streamName.set(streamName);
		this.streamTitle = new Property<String>("streamTitle", null);
		
		streams.put(streamName, this);
	}

	public void addStreamListener(IStreamListener listener) {
		writeLock.lock();
		try {
			if (!listeners.contains(listener))
				listeners.add(listener);
		} finally {
			writeLock.unlock();
		}
	}
	
	public void removeStreamListener(IStreamListener listener) {
		writeLock.lock();
		try {
			if (listeners.contains(listener))
				listeners.remove(listener);
		} finally {
			writeLock.unlock();
		}
	}

	public void clear() {
		readLock.lock();
		try {
			for(IStreamListener listener : listeners) {
				listener.streamCleared(this);
			}
		} finally {
			readLock.unlock();
		}
	}
	
	public void flush() {
		readLock.lock();
		try {
			for(IStreamListener listener : listeners) {
				listener.streamFlush(this);
			}
		} finally {
			readLock.unlock();
		}
	}
	
	public void send(String text) {
		send(new WarlockString(text));
	}
	
	public void send(WarlockString text) {
		readLock.lock();
		try {
			if (isLogging && client.getLogger() != null) {
				client.getLogger().logText(text);
			}
			
			for(IStreamListener listener : listeners) {
				try {
					listener.streamReceivedText(this, text);
				} catch (Throwable t) {
					// TODO Auto-generated catch block
					t.printStackTrace();
				}
			}
		} finally {
			readLock.unlock();
		}
		isPrompting = false;
	}
	
	public void prompt(String prompt) {
		isPrompting = true;
		
		readLock.lock();
		try {
			if (isLogging && client.getLogger() != null) {
				client.getLogger().logPrompt(prompt);
			}
			
			for (IStreamListener listener : listeners)
			{
				try {
					listener.streamPrompted(this, prompt);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		} finally {
			readLock.unlock();
		}
	}
	
	public void sendCommand(String text) {
		readLock.lock();
		try {
			if (isLogging && client.getLogger() != null) {
				client.getLogger().logEcho(text);
			}
			
			for (IStreamListener listener : listeners)
			{
				listener.streamReceivedCommand(this, text);
			}
		} finally {
			readLock.unlock();
		}
		
		isPrompting = false;
	}
	
	public boolean isPrompting () {
		return isPrompting;
	}
	
	public void echo(String text) {
		readLock.lock();
		try {
			if (isLogging && client.getLogger() != null) {
				client.getLogger().logEcho(text);
			}
			
			for (IStreamListener listener : listeners)
			{
				try {
					listener.streamEchoed(this, text);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		} finally {
			readLock.unlock();
		}
	}
	
	public IProperty<String> getName() {
		return streamName;
	}
	
	protected static Stream fromName (IWarlockClient client, String name)
	{
		if (streams.containsKey(name))
			return streams.get(name);
		
		else {
			Stream stream = new Stream(client, name);
			if (name.contains(IWarlockClient.DEFAULT_STREAM_NAME)) {
				stream.setLogging(true);
			}
			return stream;
		}
	}
	
	public static Collection<Stream> getStreams ()
	{
		return streams.values();
	}
	
	public IWarlockClient getClient ()
	{
		return client;
	}

	public IProperty<String> getTitle() {
		return streamTitle;
	}
	
	public boolean hasView() {
		return hasView;
	}
	
	public void setView(boolean view) {
		hasView = view;
	}
	
	public void updateComponent(String id, String text) {
		readLock.lock();
		try {
			for (IStreamListener listener : listeners)
			{
				try {
					listener.componentUpdated(this, id, text);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		} finally {
			readLock.unlock();
		}
	}
	
	public void setLogging (boolean logging) {
		this.isLogging = logging;
	}
}
