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
package cc.warlock.core.client.settings.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.client.settings.IHighlightProvider;
import cc.warlock.core.client.settings.IHighlightString;

@SuppressWarnings("unchecked")
public class HighlightConfigurationProvider extends ClientConfigurationProvider implements IHighlightProvider
{
	protected ArrayList<IHighlightString> highlights = new ArrayList<IHighlightString>();
	protected HashMap<String, IWarlockStyle> namedStyles = new HashMap<String, IWarlockStyle>();
	
	public HighlightConfigurationProvider ()
	{
		super("highlights");
		
		setHandleChildren(false);
	}
	
	public List<? extends IHighlightString> getHighlightStrings() {
		return highlights;
	}

	public IWarlockStyle getNamedStyle(String name) {
		if (namedStyles.containsKey(name))
		{
			return namedStyles.get(name);
		}
		return null;
	}

	public Collection<? extends IWarlockStyle> getNamedStyles() {
		return namedStyles.values();
	}
	
	@Override
	protected void parseData() {}
	
	@Override
	protected void parseChild(Element child) {
		if (child.getName().equals("highlight"))
		{
			String pattern = stringValue(child, "pattern");
			boolean literal = booleanValue(child, "literal");
			boolean caseSensitive = booleanValue(child, "caseSensitive");
			
			IWarlockStyle style = null;
			if (child.elements().size() > 0 && child.element("style") != null) {
				Element sElement = child.element("style");
				style = createStyle(sElement);
			}
			
			highlights.add(new HighlightString(this, pattern, literal, caseSensitive, style));
		}
		else if (child.getName().equals("style"))
		{
			IWarlockStyle style = createStyle(child);
			
			namedStyles.put(style.getName(), style);
		}
	}
	
	@Override
	protected void saveTo(List<Element> elements) {
		Element highlightsElement = DocumentHelper.createElement("highlights");
		
		for (IHighlightString string : highlights)
		{
			Element hElement = highlightsElement.addElement("highlight");
			hElement.addAttribute("pattern", string.getPattern().pattern());
			hElement.addAttribute("literal", ""+string.isLiteral());
			hElement.addAttribute("caseSensitive", ""+string.isCaseSensitive());
			
			createStyleElement (hElement, string.getStyle());
		}
		
		for (Map.Entry<String, IWarlockStyle> entry : namedStyles.entrySet())
		{
			highlightsElement.add(createStyleElement(entry.getValue()));
		}
		
		elements.add(highlightsElement);
	}

	protected IWarlockStyle createStyle (Element sElement)
	{
		WarlockStyle style = new WarlockStyle();
		style.setBackgroundColor(colorValue(sElement, "background"));
		style.setForegroundColor(colorValue(sElement, "foreground"));
		
		if (sElement.attributeValue("name") != null) {
			style.setName(stringValue(sElement, "name"));
		}
		
		for (Element typeElement : (List<Element>) sElement.elements()) {
			String text = typeElement.getTextTrim();
			style.addStyleType(IWarlockStyle.StyleType.valueOf(text));
		}
		
		style.setFullLine(booleanValue(sElement, "full-line"));
		
		return style;
	}
	
	protected Element createStyleElement(IWarlockStyle style) {
		return createStyleElement(null, style);
	}
	
	protected Element createStyleElement(Element parent, IWarlockStyle style)
	{
		Element element = null;
		if (parent != null) {
			element = parent.addElement("style");
		} else {
			element = DocumentHelper.createElement("style");
		}
		
		if (style.getName() != null)
			element.addAttribute("name", style.getName());
		
		element.addAttribute("background", colorString(style.getBackgroundColor()));
		element.addAttribute("foreground", colorString(style.getForegroundColor()));
		
		for (IWarlockStyle.StyleType styleType : style.getStyleTypes())
		{
			Element sElement = element.addElement("styleType");
			sElement.setText(styleType.name());
		}
		
		element.addAttribute("full-line", ""+style.isFullLine());
		
		return element;
	}
	
	public void addNamedStyle (String name, IWarlockStyle style)
	{
		namedStyles.put(name, style);
	}
	
	public void removeNamedStyle (String name)
	{
		if (namedStyles.containsKey(name)) {
			namedStyles.remove(name);
		}
	}
	
	public void addHighlightString (IHighlightString string)
	{
		highlights.add(string);
	}
	
	public void insertHighlightString(int index, IHighlightString string) {
		highlights.add(index, string);
	}
	
	public void removeHighlightString (IHighlightString string)
	{
		if (highlights.contains(string))
			highlights.remove(string);
	}
	
	public void replaceHighlightString(IHighlightString originalString,
			IHighlightString newString) {
		
		int index = highlights.indexOf(originalString);
		if (index > -1) {
			highlights.set(index, newString);
		}
	}
}
