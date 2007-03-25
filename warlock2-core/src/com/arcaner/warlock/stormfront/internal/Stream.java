/*
 * Created on Jan 16, 2005
 */
package com.arcaner.warlock.stormfront.internal;

import java.util.ArrayList;

import com.arcaner.warlock.stormfront.IStream;
import com.arcaner.warlock.stormfront.IStreamListener;

/**
 * @author Marshall
 * 
 * The internal implementation of a StormFront stream.
 */
public class Stream implements IStream {
	
	protected String streamName;
	protected ArrayList<IStreamListener> listeners;
	
	public Stream (String streamName) {
		this.streamName = streamName;
		listeners = new ArrayList<IStreamListener>();
	}

	public void addStreamListener(IStreamListener listener) {
		listeners.add(listener);
	}

	public void clear() {
		for(IStreamListener listener : listeners) {
			listener.streamCleared();
		}
	}
	
	public void send(String data) {
		for(IStreamListener listener : listeners) {
			listener.streamReceivedText(data);
		}
	}

	public String getName() {
		return streamName;
	}
}
