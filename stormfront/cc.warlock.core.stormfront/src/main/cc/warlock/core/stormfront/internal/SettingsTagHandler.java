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

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.IStormFrontTagHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class SettingsTagHandler extends DefaultTagHandler {

	private StringBuffer buffer = new StringBuffer();
	private IStormFrontTagHandler subElements;
	
	public SettingsTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		
		subElements = new SettingsElementsTagHandler(handler, this);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "settings" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		buffer.setLength(0);
		
		buffer.append(rawXML);
		/*buffer.append("<settings");
		String client = attributes.getValue("client");
		if(client != null) buffer.append(" client=\"" + client + "\"");
		String major = attributes.getValue("major");
		if(major != null) buffer.append(" major=\"" + (Integer.parseInt(major) + 1) + "\"");
		buffer.append(">\n");*/
		
		handler.getClient().startedDownloadingServerSettings();
	}
	
	@Override
	public void handleEnd(String rawXML) {
		buffer.append(rawXML);
		
		handler.getClient().finishedDownloadingServerSettings(buffer.toString());
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		buffer.append(characters);
		return true;
	}
	
	public void append(String text) {
		buffer.append(text);
	}
	
	@Override
	public IStormFrontTagHandler getTagHandler(String tagName) {
		return subElements;
	}
}
