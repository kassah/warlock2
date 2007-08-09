/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.client;


/**
 * @author Marshall
 * 
 * A Warlock Client will have 0-* IWarlockClientViewers.
 * The implementor of this class will be responsible for echoing
 * and appending text to the view of this client.  
 */
public interface IWarlockClientViewer extends IStreamListener {

	public IWarlockClient getWarlockClient ();
	
	public String getCurrentCommand ();
	
	public void setCurrentCommand (String command);
}
