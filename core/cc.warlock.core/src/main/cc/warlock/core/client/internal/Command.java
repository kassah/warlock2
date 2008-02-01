/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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
	
	public Command(String string) {
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
		this.command = command;
		this.timestamp = (Date)timestamp.clone();
	}
	
	public void setCommand(String command) {
		this.command = command;
		this.timestamp = new Date();
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
}