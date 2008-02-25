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
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockStyle.StyleType;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class DTagHandler extends DefaultTagHandler {

	WarlockStyle style;
	String command;
	boolean gotCommand = false;
	
	private class CommandRunner implements Runnable {
		private IWarlockClient client;
		private String command;
		
		CommandRunner(IWarlockClient client, String command) {
			this.client = client;
			this.command = command;
		}
		
		public void run() {
			client.send(command);
		}

	}
	
	public DTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] {"d"};
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		if(style != null) {
			handler.removeStyle(style);
			style = null;
		}
		command = null;
		String cmd = attributes.getValue("cmd");
		style = new WarlockStyle(new StyleType[] { StyleType.UNDERLINE });
		if(cmd != null) {
			style.setAction(new CommandRunner(handler.getClient(), cmd));
			gotCommand = true;
		} else {
			gotCommand = false;
		}
		handler.addStyle(style);
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		if(!gotCommand) {
			if(command == null)
				command = characters;
			else
				command += characters;
		}
		return false;
	}
	
	@Override
	public void handleEnd(String rawXML) {
		if(style != null) {
			if(command != null) {
				style.setAction(new CommandRunner(handler.getClient(), command));
			}
			handler.removeStyle(style);
			style = null;
		}
		
	}
	
	@Override
	public boolean ignoreNewlines() {
		return false;
	}
}
