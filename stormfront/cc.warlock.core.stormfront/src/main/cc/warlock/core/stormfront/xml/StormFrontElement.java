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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringEscapeUtils;

public class StormFrontElement {

	protected String name;
	protected StormFrontElement parent;
	protected StormFrontAttributeList attributes = new StormFrontAttributeList();
	protected ArrayList<StormFrontElement> elements = new ArrayList<StormFrontElement>();
	protected StringBuffer text = new StringBuffer();
	
	public StormFrontElement (String name)
	{
		this.name = name;
	}
	
	public StormFrontElement (StormFrontElement other, boolean deepCopy)
	{
		this.name = new String(other.name);
		for (StormFrontAttribute attribute : other.attributes.getList())
		{
			attributes.addAttribute(new StormFrontAttribute(attribute));
		}
		
		if (deepCopy)
		{
			for (StormFrontElement element : other.elements)
			{
				addElement(new StormFrontElement(element, deepCopy));
			}
		}
		
		this.text = other.text == null ? null : new StringBuffer(other.text);
	}
	
	public void setAttribute (String name, String value)
	{
		StormFrontAttribute attr = attributes.getAttribute(name);
		if (attr == null)
		{
			attr = new StormFrontAttribute();
			attributes.addAttribute(attr);
		}
		
		attr.setName(name);
		attr.setValue(value);
	}
	
	public void addAttribute (StormFrontAttribute attribute)
	{
		attributes.addAttribute(attribute);
	}
	
	public String removeAttribute (StormFrontAttribute attribute)
	{
		if (attribute != null && attributes.getList().contains(attribute))
		{
			attributes.removeAttribute(attribute);
		}
		return null;
	}
	
	public String removeAttribute (String attributeName)
	{
		return removeAttribute(attributes.getAttribute(attributeName));
	}
	
	public String attributeValue(String name)
	{
		return attributes.getValue(name);
	}
	
	public StormFrontAttribute attribute (String name)
	{
		return attributes.getAttribute(name);
	}
	
	public List<StormFrontAttribute> attributes ()
	{
		return Collections.unmodifiableList(attributes.getList());
	}
	
	public List<StormFrontElement> elements ()
	{
		return Collections.unmodifiableList(elements);
	}
	
	public StormFrontElement element(String name)
	{
		for (StormFrontElement element : elements)
		{
			if (element.getName().equals(name))
			{
				return element;
			}
		}
		return null;
	}
	
	public void addElement (StormFrontElement element)
	{
		elements.add(element);
		element.setParent(this);
	}
	
	public void removeElement (StormFrontElement element)
	{
		if (elements.remove(element))
			element.setParent(null);
	}
	
	public void setParent (StormFrontElement parent)
	{
		this.parent = parent;
	}
	
	public StormFrontElement getParent ()
	{
		return this.parent;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getText ()
	{
		return StringEscapeUtils.unescapeXml(text.toString());
	}
	
	public String getTextTrim ()
	{
        StringBuffer textContent = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(getText());

        while (tokenizer.hasMoreTokens()) {
            String str = tokenizer.nextToken();
            textContent.append(str);

            if (tokenizer.hasMoreTokens()) {
                textContent.append(" "); // separator
            }
        }

        return textContent.toString();
	}
	
	public void appendText (String text)
	{
		this.text.append(text);
	}
	
	public void setText (String text)
	{
		this.text.setLength(0);
		this.text.append(text);
	}
	
	public String toXML (String prefix, boolean prettyPrint, boolean includeChildren)
	{
		StringBuffer xml = new StringBuffer();
		
		xml.append(prefix + "<" + name);
		if (attributes.getList().size() > 0)
		{
			xml.append(" ");
			for (Iterator<StormFrontAttribute> iter = attributes.getList().iterator(); iter.hasNext();)
			{
				StormFrontAttribute attribute = iter.next();
				
				xml.append(attribute.getName());
				xml.append("=");
				xml.append("\"");
				xml.append(StringEscapeUtils.escapeXml(attribute.getValue()));
				xml.append("\"");
				
				if (iter.hasNext())
					xml.append(" ");
			}
		}
		
		boolean hasContent = false;
		if (elements.size() > 0 || getText().length() > 0)
		{
			hasContent = true;
			
			xml.append(">");
			if (prettyPrint)
				xml.append("\n");
		}
		
		if (elements.size() > 0)
		{
			if (includeChildren)
			{
				for (StormFrontElement element : elements)
				{
					xml.append(element.toXML(prettyPrint ? prefix + "  " : "", prettyPrint,  true));
				}
			}
		}
		if (getText().length()> 0)
		{
			xml.append(StringEscapeUtils.escapeXml(getText()));
		}
		
		if (hasContent)
		{
			xml.append (/* FIXME can't do this because it gets passed on as content: prefix +*/ "</"+ name + ">");
			if (prettyPrint)
				xml.append("\n");
		}
		else
		{
			xml.append ("/>");
			if (prettyPrint)
				xml.append("\n");
		}
		return xml.toString();
	}
}
