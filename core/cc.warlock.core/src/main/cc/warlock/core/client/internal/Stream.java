/*
 * Created on Jan 16, 2005
 */
package cc.warlock.core.client.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.WarlockString;


/**
 * @author Marshall
 * 
 * The internal implementation of a StormFront stream.
 */
public class Stream implements IStream {
	
	protected static Hashtable<String, Stream> streams = new Hashtable<String, Stream>();
	
	protected IProperty<String> streamName, streamTitle;
	private ArrayList<IStreamListener> listeners = new ArrayList<IStreamListener>();
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	protected boolean isPrompting = false;
	
	protected Stream (String streamName) {
		this.streamName = new Property<String>("streamName", null);
		this.streamName.set(streamName);
		this.streamTitle = new Property<String>("streamTitle", null);
		
		streams.put(streamName, this);
	}

	public void addStreamListener(IStreamListener listener) {
		lock.writeLock().lock();
		try {
			if (!listeners.contains(listener))
				listeners.add(listener);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	public void removeStreamListener(IStreamListener listener) {
		lock.writeLock().lock();
		try {
			if (listeners.contains(listener))
				listeners.remove(listener);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void clear() {
		lock.readLock().lock();
		try {
			for(IStreamListener listener : listeners) {
				listener.streamCleared(this);
			}
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public void send(String text) {
		send(new WarlockString(text));
	}
	
	public void send(WarlockString text) {
		lock.readLock().lock();
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
			lock.readLock().unlock();
		}
		isPrompting = false;
	}
	
	public void prompt(String prompt) {
		isPrompting = true;
		
		lock.readLock().lock();
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
			lock.readLock().unlock();
		}
	}
	
	public void sendCommand(String text) {
		lock.readLock().lock();
		try {
			for (IStreamListener listener : listeners)
			{
				listener.streamReceivedCommand(this, text);
			}
		} finally {
			lock.readLock().unlock();
		}
		
		isPrompting = false;
	}
	
	public boolean isPrompting () {
		return isPrompting;
	}
	
	public void echo(String text) {
		lock.readLock().lock();
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
			lock.readLock().unlock();
		}
	}
	
	public IProperty<String> getName() {
		return streamName;
	}
	
	protected static Stream fromName (String name)
	{
		if (streams.containsKey(name))
			return streams.get(name);
		
		else return new Stream(name);
	}
	
	public static Collection<Stream> getStreams ()
	{
		return streams.values();
	}
	
	public IWarlockClient getClient ()
	{
		for (IWarlockClient client : WarlockClientRegistry.getActiveClients())
		{
			if (client instanceof WarlockClient)
			{
				WarlockClient c = (WarlockClient) client;
				if (getName().get().indexOf(c.streamPrefix) > -1)
				{
					return c;
				}
			}
		}
		return null;
	}

	public IProperty<String> getTitle() {
		return streamTitle;
	}
}
