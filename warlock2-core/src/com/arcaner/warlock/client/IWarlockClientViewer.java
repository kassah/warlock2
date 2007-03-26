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
public interface IWarlockClientViewer {

	public void append (String text);
	
	public void append (String viewName, String text);
	
	public void echo (String text);
	
	public IWarlockClient getWarlockClient ();
	
	public void setViewerTitle (String title);
	
	public String getCurrentCommand ();
}
