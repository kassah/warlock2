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
package cc.warlock.core.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

@SuppressWarnings("unchecked")
public abstract class TreeConfigurationProvider implements
		IConfigurationProvider {
	
	protected TreeConfigurationProvider parentProvider = null;
	protected ArrayList<IConfigurationProvider> childProviders = new ArrayList<IConfigurationProvider>();
	protected ArrayList<Element> unhandledElements = new ArrayList<Element>();
	protected String elementName;
	protected boolean handleChildren = false;
	
	public TreeConfigurationProvider (String elementName)
	{
		this.elementName = elementName;
	}
	
	protected void setHandleChildren (boolean handleChildren) {
		this.handleChildren = handleChildren;
	}
	
	public void addChildProvider(IConfigurationProvider provider)
	{
		childProviders.add(provider);
		
		if (provider instanceof TreeConfigurationProvider) {
			((TreeConfigurationProvider)provider).parentProvider = this;
		}
		
		parseUnhandledElements();
	}
	
	protected void parseUnhandledElements ()
	{
		if (handleChildren)
		{
			for (Iterator<Element> iter = unhandledElements.iterator(); iter.hasNext(); )
			{
				Element element = iter.next();
				for (IConfigurationProvider provider : childProviders)
				{
					if (provider.supportsElement(element))
					{
						provider.parseElement(element);
						iter.remove();
					}
				}
			}
		}
	}
	
	protected Element element;
	
	public void parseElement(Element element)
	{
		this.element = element;
		
		parseData();
		
		for (Element subElement: (List<Element>) element.elements())
		{
			if (handleChildren)
			{
				unhandledElements.add(subElement);
				
				parseUnhandledElements();
			} else {
				parseChild(subElement);
			}
		}
	}
	
	/**
	 * Extenders should override this method if they intend to handle their own child nodes
	 * @param child a child element
	 */
	protected void parseChild (Element child)
	{
	}
	
	protected static boolean booleanValue(Element element, String attr)
	{
		return Boolean.parseBoolean(element.attributeValue(attr));
	}
	
	protected boolean booleanValue(String attr)
	{
		return booleanValue(element, attr);
	}
	
	protected static int intValue(Element element, String attr)
	{
		return Integer.parseInt(element.attributeValue(attr));
	}
	
	protected int intValue(String attr)
	{
		return intValue(element, attr);
	}
	
	protected String stringValue(Element element, String attr)
	{
		return element.attributeValue(attr);
	}
	
	protected String stringValue(String attr)
	{
		return stringValue(element, attr);
	}
	
	protected Enum<?> enumValue(Class<? extends Enum> enumClass, String attr) {
        return Enum.valueOf(enumClass, stringValue(attr));
    }
	
	protected abstract void parseData();
	

	public boolean supportsElement(Element element) {
		return element.getName().equals(elementName);
	}

	public List<Element> getTopLevelElements() {
		ArrayList<Element> elements = new ArrayList<Element>();
		
		saveTo(elements);
		
		return elements;
	}
	
	protected abstract void saveTo(List<Element> elements);
}
