/*
 * Created on Jan 16, 2005
 */
package cc.warlock.core.client;


/**
 * @author Marshall
 *
 * IStreamListener implementations will subscribe to an IStream and receive an event when the Stream receives new data.
 */
public interface IStreamListener {

	public void streamAddedStyle (IStream stream, IWarlockStyle style);
	public void streamRemovedStyle (IStream stream, IWarlockStyle style);
	public void streamReceivedText (IStream stream, WarlockString text);
	
	public void streamPrompted (IStream stream, String prompt);
	public void streamReceivedCommand (IStream stream, String text);
	
	public void streamEchoed (IStream stream, String text);
	
	public void streamCleared (IStream stream);
}
