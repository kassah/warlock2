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
package cc.warlock.core.stormfront;

import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


/**
 * @author Marshall
 * 
 * Implementors of this interface will supply a list of XPath expressions which will be tested on the
 * received XML document from StormFront, and if any (or all -- depending on the implementation) match,
 * the interfaces "handleNodes" method will be called with the subsequent matching nodeset of the matching
 * XPath.
 * 
 * If multiple XPaths are matched, handleNodes will be called for each match.
 */
public interface IStormFrontTagHandler {
	
	public String[] getTagNames();
	
	public String getCurrentTag();
	public void setCurrentTag(String tagName);
	
	public IStormFrontTagHandler getTagHandler(String tagName);
	
	/**
	 * Handle the start tag 
	 * @param atts
	 */
	public void handleStart(StormFrontAttributeList attributes, String rawXML);
	
	/**
	 * handle the end tag
	 * @param
	 */
	public void handleEnd(String rawXML);
	
	/**
	 * handle contents of the tag
	 * @param characters
	 */
	public boolean handleCharacters(String characters);
	
	public boolean ignoreNewlines();
}
