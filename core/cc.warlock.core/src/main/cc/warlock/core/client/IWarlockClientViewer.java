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
	
	public String getCurrentCommand ();
	
	public void setCurrentCommand (String command);
	
	public void append(char c);
	
	public void submit();
	
	public void prevCommand();
	
	public void nextCommand();
	
	public void searchHistory();
	
	public void repeatLastCommand();
	
	public void repeatSecondToLastCommand();
}
