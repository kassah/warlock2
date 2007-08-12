/*
 * Created on Jan 15, 2005
 */
package cc.warlock.client;

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
	 * @return whether this command is the last command in the command history
	 */
	public boolean isLast();
	
	public void setLast (boolean last);
	
	/**
	 * @return whether this command is the first command in the command history
	 */
	public boolean isFirst();
	
	public void setFirst (boolean first);
	
	/**
	 * @return whether this command is the command history.
	 */
	public boolean isInHistory();
	
	public void setInHistory (boolean inHistory);
}
