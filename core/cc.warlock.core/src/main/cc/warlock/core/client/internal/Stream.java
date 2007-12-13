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
		
		else return new Stream(client, name);
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
}
