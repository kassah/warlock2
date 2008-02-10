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
package cc.warlock.core.stormfront.settings.server;

import java.util.regex.Pattern;

import cc.warlock.core.stormfront.xml.StormFrontElement;

@Deprecated
public class IgnoreSetting extends ServerSetting {

	private String text;
	private boolean matchPartialWord = false;
	private boolean ignoreCase = false;
	private Pattern regex;
	
	public IgnoreSetting (ServerSettings settings, StormFrontElement ignoreElement)
	{
		super(settings, ignoreElement);
		
		this.text = ignoreElement.attributeValue("text");
	}
	
	@Override
	protected void saveToDOM() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String toStormfrontMarkup() {
		// TODO Auto-generated method stub
		return null;
	}

	public Pattern getRegex() {
		if(regex == null) {
			String regText = Pattern.quote(text);
			if(!matchPartialWord)
				regText = "\\b" + regText + "\\b";
			int flags = 0;
			if(ignoreCase)
				flags |= Pattern.CASE_INSENSITIVE;
			regex = Pattern.compile(regText, flags);
		}

		return regex;
	}

	public void setText(String text) {
		this.text = text;
		regex = null;
	}

	public void setMatchPartialWord(boolean matchPartialWord) {
		this.matchPartialWord = matchPartialWord;
		regex = null;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		regex = null;
	}

}
