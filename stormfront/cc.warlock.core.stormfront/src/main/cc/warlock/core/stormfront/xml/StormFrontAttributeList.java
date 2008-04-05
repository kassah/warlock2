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
package cc.warlock.core.stormfront.xml;

import java.util.ArrayList;
import java.util.List;

public class StormFrontAttributeList {

	protected List<StormFrontAttribute> attributes = new ArrayList<StormFrontAttribute>();
	
	public void addAttribute (StormFrontAttribute attribute)
	{
		attributes.add(attribute);
	}
	
	public void removeAttribute (StormFrontAttribute attribute)
	{
		attributes.remove(attribute);
	}
	
	public void clear ()
	{
		attributes.clear();
	}
	
	public StormFrontAttribute getAttribute (String attributeName)
	{
		for (StormFrontAttribute attribute : attributes)
		{
			if (attribute.getName().equals(attributeName))
			{
				return attribute;
			}
		}
		return null;
	}
	
	public String getValue (String attributeName)
	{
		StormFrontAttribute attr = getAttribute(attributeName);
		if (attr != null)
		{
			return attr.getValue();
		}
		return null;
	}
	
	public List<StormFrontAttribute> getList()
	{
		return attributes;
	}
}
