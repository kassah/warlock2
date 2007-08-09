/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.client;

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
}
