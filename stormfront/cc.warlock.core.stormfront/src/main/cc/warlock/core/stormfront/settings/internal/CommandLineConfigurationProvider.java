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
package cc.warlock.core.stormfront.settings.internal;

import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.WarlockFont;
import cc.warlock.core.client.settings.internal.ClientConfigurationProvider;
import cc.warlock.core.stormfront.settings.ICommandLineSettings;
import cc.warlock.core.stormfront.settings.ICommandLineSettingsProvider;

/**
 * @author marshall
 *
 */
public class CommandLineConfigurationProvider extends
		ClientConfigurationProvider implements ICommandLineSettingsProvider {

	protected CommandLineSettings settings;
	
	public CommandLineConfigurationProvider ()
	{
		super("command-line");
	}
	
	@Override
	protected void parseData() {
		settings = new CommandLineSettings(this);
		settings.setBackgroundColor(new WarlockColor(stringValue("background")));
		settings.setForegroundColor(new WarlockColor(stringValue("foreground")));
		settings.setBarColor(new WarlockColor(stringValue("barColor")));
		
		WarlockFont font = new WarlockFont();
		font.setFamilyName(stringValue("fontFace"));
		font.setSize(intValue("fontSize"));
		
		settings.setFont(font);
	}

	@Override
	protected void saveTo(List<Element> elements) {
		Element cElement = DocumentHelper.createElement("command-line");
		elements.add(cElement);
		
		cElement.addAttribute("background", settings.getBackgroundColor().toHexString());
		cElement.addAttribute("foreground", settings.getForegroundColor().toHexString());
		cElement.addAttribute("barColor", settings.getBarColor().toHexString());
		cElement.addAttribute("fontFace", settings.getFont().getFamilyName());
		cElement.addAttribute("fontSize", ""+settings.getFont().getSize());
	}

	public ICommandLineSettings getCommandLineSettings() {
		return settings;
	}

}
