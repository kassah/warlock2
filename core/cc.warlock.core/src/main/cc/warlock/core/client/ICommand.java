/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.client;

import java.util.Date;

/**
 * @author Marshall
 * 
 * A command in the command history. Simply has two properties: The command itself in string form, and the timestamp it was entered on.
 */
public interface ICommand {
	/**
	 * @return The command
	 */
	public String getCommand();
        
	/**
	 * @return The timestamp for this command
	 */
	public Date getTimestamp();
	
	/**
	 * @return whether this command will go into the history. If it's already in the history, this will return false.
	 */
	public boolean isBoundForHistory();
	
	public void setBoundForHistory(boolean inHistory);
}
