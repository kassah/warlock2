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

	public void streamReceivedText (IStream stream, IStyledString text);
	
	public void streamPrompted (IStream stream, String prompt);
	public void streamDonePrompting (IStream stream);
	
	public void streamEchoed (IStream stream, String text);
	
	public void streamCleared (IStream stream);
}
