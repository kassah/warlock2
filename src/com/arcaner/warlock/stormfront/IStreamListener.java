/*
 * Created on Jan 16, 2005
 */
package com.arcaner.warlock.stormfront;

/**
 * @author Marshall
 *
 * IStreamListener implementations will subscribe to an IStream and receive an event when the Stream receives new data.
 */
public interface IStreamListener {

	public void streamReceivedText (String text);
	
	public void streamCleared ();
}
