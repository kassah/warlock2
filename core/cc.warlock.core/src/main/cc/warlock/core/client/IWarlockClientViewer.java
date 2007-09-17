/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.client;


/**
 * @author Marshall
 * 
 * A Warlock Client will have 0-* IWarlockClientViewers.
 * The implementor of this class will be responsible for echoing
 * and appending text to the view of this client.  
 */
public interface IWarlockClientViewer extends IStreamListener {

	public IWarlockClient getWarlockClient ();
	
	public ICommand getCurrentCommand ();
	
	public void setCurrentCommand (ICommand command);
}
