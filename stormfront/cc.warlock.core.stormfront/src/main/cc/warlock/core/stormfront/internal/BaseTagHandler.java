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

import java.util.HashMap;
import java.util.Map;

import cc.warlock.core.stormfront.IStormFrontTagHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

abstract public class BaseTagHandler implements IStormFrontTagHandler {
	private String currentTag;
	private Map<String, IStormFrontTagHandler> tagHandlers;
	
	public abstract String[] getTagNames();
	
	public void handleStart(StormFrontAttributeList attributes, String rawXML) { }

	public void handleEnd(String rawXML) { }

	public boolean handleCharacters(String characters) {
		return false;
	}
	
	public String getCurrentTag() {
		return currentTag;
	}
	
	public void setCurrentTag(String tagName) {
		this.currentTag = tagName;
	}
	
	private void addTagHandler(String tagName, IStormFrontTagHandler tagHandler) {
		if(tagHandlers == null) {
			tagHandlers = new HashMap<String, IStormFrontTagHandler>();
		}
		tagHandlers.put(tagName, tagHandler);
	}
	
	protected void addTagHandler(IStormFrontTagHandler tagHandler) {
		for(String tagName : tagHandler.getTagNames()) {
			addTagHandler(tagName, tagHandler);
		}
	}
	
	public IStormFrontTagHandler getTagHandler(String tagName) {
		if(tagHandlers == null)
			return null;
		
		return tagHandlers.get(tagName);
	}
}
