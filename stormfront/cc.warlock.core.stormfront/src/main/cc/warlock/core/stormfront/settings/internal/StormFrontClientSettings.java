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

import cc.warlock.core.client.settings.IWindowSettings;
import cc.warlock.core.client.settings.internal.ClientSettings;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.settings.ICommandLineSettings;
import cc.warlock.core.stormfront.settings.IStormFrontClientSettings;

/**
 * @author marshall
 *
 */
public class StormFrontClientSettings extends ClientSettings implements
		IStormFrontClientSettings {

	public static final String WINDOW_MAIN = "smain";
	public static final String WINDOW_INVENTORY = "sinv";
	public static final String WINDOW_SPELLS = "sSpells";
	public static final String WINDOW_DEATHS = "sdeath";
	public static final String WINDOW_THOUGHTS = "sthoughts";
	
	public static final String PRESET_BOLD = "bold";
	public static final String PRESET_ROOM_NAME = "roomName";
	public static final String PRESET_SPEECH = "speech";
	public static final String PRESET_WHISPER = "whisper";
	public static final String PRESET_THOUGHT = "thought";
	public static final String PRESET_WATCHING = "watching";
	public static final String PRESET_LINK = "link";
	public static final String PRESET_SELECTED_LINK = "selectedLink";
	public static final String PRESET_COMMAND = "command";
	
	protected CommandLineConfigurationProvider commandLineProvider;
	protected IStormFrontClient sfClient;
	
	public StormFrontClientSettings (IStormFrontClient client)
	{
		super(client);
		this.sfClient = client;
		
		commandLineProvider = new CommandLineConfigurationProvider();
		addChildProvider(commandLineProvider);
		addClientSettingProvider(commandLineProvider);
	}
	
	public IStormFrontClient getStormFrontClient() {
		return sfClient;
	}
	
	public ICommandLineSettings getCommandLineSettings() {
		return commandLineProvider.getCommandLineSettings();
	}

	public IWindowSettings getMainWindowSettings() {
		return getWindowSettings(WINDOW_MAIN);
	}

}
