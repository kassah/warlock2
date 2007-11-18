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
			attr.setQuoteType('"');
		}
		
		attr.setName(name);
		attr.setValue(value);
	}
	
	public void addAttribute (StormFrontAttribute attribute)
	{
		attributes.addAttribute(attribute);
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
		if (elements.contains(element))
		{
			elements.remove(element);
			element.setParent(null);
		}
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
		return getTextTrim();
	}
	
	public String getTextTrim ()
	{
        StringBuffer textContent = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(text.toString());

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
				xml.append(attribute.getQuoteType());
				xml.append(StringEscapeUtils.escapeXml(attribute.getValue()));
				xml.append(attribute.getQuoteType());
				
				if (iter.hasNext())
					xml.append(" ");
			}
		}
		
		boolean hasContent = false;
		if (elements.size() > 0 || getTextTrim().length() > 0)
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
		if (getTextTrim().length()> 0)
		{
			xml.append(StringEscapeUtils.escapeXml(getTextTrim()));
		}
		
		if (hasContent)
		{
			xml.append ("</"+ name + ">");
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
