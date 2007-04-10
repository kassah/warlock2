/*
 * Created on Jan 16, 2005
 */
package com.arcaner.warlock.stormfront.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.stormfront.IStormFrontStyle;
import com.arcaner.warlock.stormfront.IStream;
import com.arcaner.warlock.stormfront.IStreamListener;

/**
 * @author Marshall
 * 
 * The internal implementation of a StormFront stream.
 */
public class Stream implements IStream {
	
	private static Hashtable<String, Stream> streams = new Hashtable<String, Stream>();
	public static final Stream DEFAULT_STREAM = new Stream(IWarlockClient.DEFAULT_VIEW);
	
	protected String streamName, streamTitle;
	protected ArrayList<IStreamListener> listeners;
	
	private Stream (String streamName) {
		this.streamName = streamName;
		listeners = new ArrayList<IStreamListener>();
		
		streams.put(streamName, this);
	}

	public void addStreamListener(IStreamListener listener) {
		listeners.add(listener);
	}
	
	public void removeStreamListener(IStreamListener listener) {
		listeners.remove(listener);
	}

	public void clear() {
		for(IStreamListener listener : listeners) {
			listener.streamCleared();
		}
	}
	
	public void send(String data, IStormFrontStyle style) {
		for(IStreamListener listener : listeners) {
			listener.streamReceivedText(data, style);
		}
	}

	public String getName() {
		return streamName;
	}
	
	public static Stream fromName (String name)
	{
		if (streams.containsKey(name))
			return streams.get(name);
		
		else return new Stream(name);
	}
	
	public static Collection<Stream> getStreams ()
	{
		return streams.values();
	}

	public String getTitle() {
		return streamTitle;
	}

	public void setTitle(String streamTitle) {
		this.streamTitle = streamTitle;
	}
}
