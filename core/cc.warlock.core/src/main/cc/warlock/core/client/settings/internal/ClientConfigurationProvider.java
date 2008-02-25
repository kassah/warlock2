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

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.WarlockFont;
import cc.warlock.core.client.settings.IClientSettingProvider;
import cc.warlock.core.configuration.TreeConfigurationProvider;

/**
 * An implementation class for setting providers, based on our XML configuration backend.
 * @author marshall
 *
 */
public abstract class ClientConfigurationProvider extends TreeConfigurationProvider implements IClientSettingProvider {

	public ClientConfigurationProvider (String elementName)
	{
		super(elementName);
	}
	
	protected WarlockFont elementToFont (Element element)
	{
		if (element == null) return WarlockFont.DEFAULT_FONT;
		if (element.attributeValue("family").equals("default")) return WarlockFont.DEFAULT_FONT;
		
		WarlockFont font = new WarlockFont();
		font.setFamilyName(element.attributeValue("family"));
		font.setSize(Integer.parseInt(element.attributeValue("size")));
		return font;
	}
	
	protected Element fontToElement (WarlockFont font, String elementName)
	{
		Element fontEl = DocumentHelper.createElement(elementName);
		addFontAttributes(font, fontEl);
		
		return fontEl;
	}
	
	protected void addFontAttributes (WarlockFont font, Element fontEl)
	{
		fontEl.addAttribute("family", (font == null || font.getFamilyName() == null || font.getFamilyName().equals("")) ? "default" : font.getFamilyName());
		fontEl.addAttribute("size", (font == null || font.getSize() == -1) ? "default" : ""+font.getSize());
	}

	protected String colorString (WarlockColor color)
	{
		if (color == null || color == WarlockColor.DEFAULT_COLOR) return "default";
		return color.toHexString();
	}
	
	protected WarlockColor colorValue (String attribute) {
		return colorValue(this.element, attribute);
	}
	
	protected WarlockColor colorValue (Element element, String attribute) {
		String value = stringValue(element, attribute);
		if (value == null) return null;
		
		if (value.equals("default")) {
			return WarlockColor.DEFAULT_COLOR;
		}
		return new WarlockColor(value);
	}
}
