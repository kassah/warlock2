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

import org.dom4j.Element;

import cc.warlock.core.client.settings.IPatternSetting;

/**
 * @author marshall
 *
 */
public abstract class PatternConfigurationProvider extends ClientConfigurationProvider {

	public PatternConfigurationProvider (String elementName)
	{
		super(elementName);
	}
	
	protected void fillElement (Element element, IPatternSetting setting)
	{
		element.addAttribute("pattern", setting.getText());	
		element.addAttribute("literal", ""+setting.isLiteral());
		element.addAttribute("caseSensitive", ""+setting.isCaseSensitive());
		element.addAttribute("fullWordMatch", ""+setting.isFullWordMatch());
	}

	protected void fillSetting (IPatternSetting setting, Element element)
	{
		String pattern = stringValue(element, "pattern");
		boolean literal = booleanValue(element, "literal");
		boolean caseSensitive = booleanValue(element, "caseSensitive");
		boolean fullWordMatch = true;
		if (element.attributeValue("fullWordMatch") == null) {
			fullWordMatch = true;
		} else {
			fullWordMatch = booleanValue(element, "fullWordMatch");
		}
		
		if (fullWordMatch && literal) {
			literal = false;
		}
		
		setting.setText(pattern);
		setting.setLiteral(literal);
		setting.setCaseSensitive(caseSensitive);
		setting.setFullWordMatch(fullWordMatch);
	}
}
