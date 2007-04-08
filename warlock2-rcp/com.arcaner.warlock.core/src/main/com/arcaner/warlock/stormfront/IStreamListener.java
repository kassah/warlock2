/*
 * Created on Jan 16, 2005
 */
package com.arcaner.warlock.stormfront;

import com.arcaner.warlock.client.stormfront.IStormFrontStyle;

/**
 * @author Marshall
 *
 * IStreamListener implementations will subscribe to an IStream and receive an event when the Stream receives new data.
 */
public interface IStreamListener {

	public void streamReceivedText (String text, IStormFrontStyle style);
	
	public void streamCleared ();
}
