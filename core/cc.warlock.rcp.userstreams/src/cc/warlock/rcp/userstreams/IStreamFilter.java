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
/**
 * 
 */
package cc.warlock.rcp.userstreams;

import cc.warlock.core.client.WarlockString;

/**
 * @author Will Robertson
 * Interface for storing a stream filter
 */
public interface IStreamFilter {
	public enum type { string, regex }
	
	/*
	 * Does the text matches this filter?
	 */
	public boolean match(String text);
	public boolean match(WarlockString text);
	
	/*
	 * Sets the type of filter { string, regex }
	 */
	public void setType(type filterType);
	
	/*
	 * Get the type of filter { string, regex }
	 */
	public type getType();
	
	/*
	 * Sets the content of the filter to match against
	 */
	public void setContent(String content);
	
	/*
	 * Get the content of the filter
	 */
	public String getContent();
}
