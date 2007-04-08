/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.stormfront;

import com.arcaner.warlock.client.stormfront.IStormFrontStyle;

/**
 * @author Marshall
 *
 * A stream is StormFront's idea of a text buffer for a window. This is the Interface representing that buffer.
 */
public interface IStream {
	
	public void clear();
	
	public String getName();
	
	public void send(String data, IStormFrontStyle style);
	
	public void addStreamListener(IStreamListener listener);
}
