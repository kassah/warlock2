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
package cc.warlock.core.stormfront.settings;

import java.io.InputStream;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cc.warlock.core.client.settings.IHighlightString;
import cc.warlock.core.client.settings.internal.ClientConfigurationProvider;
import cc.warlock.core.stormfront.settings.internal.StormFrontClientSettings;
import cc.warlock.core.stormfront.settings.server.IgnoreSetting;
import cc.warlock.core.stormfront.settings.server.Preset;
import cc.warlock.core.stormfront.settings.server.ServerScript;
import cc.warlock.core.stormfront.settings.server.ServerSettings;


/**
 * The purpose of this class is to import/export (and eventually merge) StormFront's server settings
 * @author marshall
 *
 */
@SuppressWarnings("deprecation")
public class StormFrontServerSettings extends ClientConfigurationProvider {
	
	protected static StormFrontServerSettings _instance;
	
	protected String version;
	protected String crc;
	
	protected StormFrontServerSettings ()
	{
		super("stormfront-settings");
	}
	
	protected static StormFrontServerSettings instance() {
		if (_instance == null) {
			_instance = new StormFrontServerSettings();
		}
		return _instance;
	}
	
	public static void importSettings (InputStream stream, StormFrontClientSettings settings)
	{
		instance().importServerSettings(stream, settings);
	}
	
	protected void importServerSettings (InputStream stream, StormFrontClientSettings settings)
	{
		ServerSettings serverSettings = new ServerSettings(settings.getStormFrontClient());
		serverSettings.load(settings.getStormFrontClient().getClientId(), stream);
		
		settings.addChildProvider(this);
		settings.addClientSettingProvider(this);
		
		for (IHighlightString string : serverSettings.getHighlightStrings())
		{
			importHighlightString(string);
		}
		
		for (Preset preset : serverSettings.getPresets())
		{
			importPreset(preset);
		}
		
		importCommandLineSettings(serverSettings.getCommandLineSettings());
		
		for (cc.warlock.core.stormfront.settings.server.WindowSettings wSettings : serverSettings.getAllWindowSettings())
		{
			importWindowSettings(wSettings);
		}
		
		for (ServerScript script : serverSettings.getAllServerScripts())
		{
			importScript(script);
		}
		
		for (String varName : serverSettings.getVariableNames())
		{
			importVariable(varName, serverSettings.getVariable(varName));
		}
		
		for (IgnoreSetting ignore : serverSettings.getIgnores())
		{
			importIgnore(ignore);
		}
	}
	
	protected void importHighlightString (IHighlightString string)
	{
		
	}
	
	protected void importPreset (Preset preset)
	{
		
	}
	
	protected void importCommandLineSettings (cc.warlock.core.stormfront.settings.server.CommandLineSettings settings)
	{
		
	}
	
	protected void importWindowSettings (cc.warlock.core.stormfront.settings.server.WindowSettings settings)
	{
		
	}
	
	protected void importIgnore (IgnoreSetting ignore)
	{
		
	}
	
	protected void importVariable (String var, String value)
	{
		
	}
	
	protected void importScript (ServerScript script)
	{
		
	}
	
	@Override
	protected void parseData() {
		version = stringValue("version");
		crc = stringValue("crc");
	}
	
	@Override
	protected void saveTo(List<Element> elements) {
		Element element = DocumentHelper.createElement("stormfront-settings");
		elements.add(element);
		
		element.addAttribute("version", version);
		element.addAttribute("crc", crc);
	}
}
