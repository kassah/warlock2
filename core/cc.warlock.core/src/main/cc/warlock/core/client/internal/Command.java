/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.client.internal;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import cc.warlock.core.client.ICommand;

/**
 * @author Marshall
 *
 * A simple Command in the Command History implementation
 */
public class Command implements ICommand, Serializable {
	private static final long serialVersionUID = 51L;

	protected String command;
	protected Date timestamp;
	protected boolean first, last, inHistory;
	
	public Command(String string) {
		first = last = inHistory = false;
		
		// TODO make the following line work for all locales.
		DateFormat dateFormat = DateFormat.getTimeInstance();
		
		String[] strs = string.split(",", 2);
		command = strs[0];
		try {
			timestamp = dateFormat.parse(strs[1]);
		} catch(ParseException e) {
			e.printStackTrace();
		}
	}
	
	public Command(String command, Date timestamp)
	{
		first = last = inHistory = false;
		
		this.command = command;
		this.timestamp = (Date)timestamp.clone();
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}

	public Date getTimestamp() {
		return (Date)timestamp.clone();
	}

	public String toString() {
		return "(" + command + "," + timestamp.toString() + ")";
	}
	
	public boolean isFirst() {
		 return first;
	}
	
	public boolean isLast() {
		return last;
	}
	
	public boolean isInHistory() {
		return inHistory;
	}
	
	public void setFirst (boolean first) {
		this.first = first;
	}
	
	public void setLast (boolean last) {
		this.last = last;
	}
	
	public void setInHistory(boolean inHistory) {
		this.inHistory = inHistory;
	}
}