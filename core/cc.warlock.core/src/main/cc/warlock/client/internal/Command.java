/*
 * Created on Jan 15, 2005
 */
package cc.warlock.client.internal;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import cc.warlock.client.ICommand;

/**
 * @author Marshall
 *
 * A simple Command in the Command History implementation
 */
public class Command implements ICommand, Serializable {
	private static final long serialVersionUID = 51L;

	protected String command;
	protected Date timestamp;
	// TODO make the following line work for all locales.
	protected static DateFormat dateFormat = DateFormat.getTimeInstance();
	
	public Command(String string) {
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
		this.command = command;
		this.timestamp = timestamp;
	}
	
	public String getCommand() {
		return command;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String toString() {
		return "(" + command + "," + timestamp.toString() + ")";
	}
}