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
	protected boolean fromScript = false;
	protected String prefix;
	
	public Command(String command) {
		this.command = command + "\n";
		timestamp = new Date();
	}
	
	public Command(String command, boolean fromScript) {
		this(command);
		this.fromScript = fromScript;
	}
	
	public void setCommand(String command) {
		this.command = command;
		// FIXME: why is the following line here?
		this.timestamp = new Date();
	}
	
	public String getCommand() {
		return command;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String toString() {
		return "(" + command + "," + timestamp.toString() + ")";
	}
	
	public String getText() {
		if(prefix != null)
			return prefix + command;
		else
			return command;
	}
	
	public boolean fromScript() {
		return fromScript;
	}
}