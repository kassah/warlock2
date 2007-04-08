/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.stormfront;

/**
 * @author Marshall
 *
 * A stream is StormFront's idea of a text buffer for a window. This is the Interface representing that buffer.
 */
public interface IStream {
	
	public void clear();
	
	public String getName();
	
	public void send(String data);
	
	public void addStreamListener(IStreamListener listener);
}
