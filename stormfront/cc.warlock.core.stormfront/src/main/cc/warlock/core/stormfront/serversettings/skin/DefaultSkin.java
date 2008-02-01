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
package cc.warlock.core.stormfront.serversettings.skin;

import java.util.Hashtable;
import java.util.Map;

import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.stormfront.client.StormFrontColor;
import cc.warlock.core.stormfront.serversettings.server.ColorSetting;
import cc.warlock.core.stormfront.serversettings.server.Preset;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;

/**
 * The default skin handles any attributes who's values are "skin"
 * @author marshall
 */
public class DefaultSkin implements IStormFrontSkin {

	public static final int DEFAULT_FONT_SIZE = 12;
	public static final StormFrontColor MAIN_COLOR = new StormFrontColor(-1, -1, -1);
	
	protected Hashtable<String, StormFrontColor> fgColors = new Hashtable<String, StormFrontColor>();
	protected Hashtable<String, StormFrontColor> bgColors = new Hashtable<String, StormFrontColor>();
	protected StormFrontColor commandLineBarColor;
	protected ServerSettings settings;
	
	protected StormFrontColor defaultWindowBackground, defaultWindowForeground;
	
	protected static StormFrontColor skinColor (String color)
	{
		StormFrontColor c = new StormFrontColor(color);
		c.setSkinColor(true);
		return c;
	}
	
	public DefaultSkin (ServerSettings settings)
	{
		this.settings = settings;
		
		fgColors.put("bold", skinColor("#FFFF00"));
		fgColors.put("roomName", skinColor("#FFFFFF"));
		fgColors.put("speech", skinColor("#80FF80"));
		fgColors.put("thought", skinColor("#FF8000"));
		fgColors.put("cmdline", skinColor("#FFFFFF"));
		fgColors.put("whisper", skinColor("#80FFFF"));
		fgColors.put("watching", skinColor("#FFFF00"));
		fgColors.put("link", skinColor("#62B0FF"));
		fgColors.put("selectedLink", skinColor("#000000"));
		fgColors.put("command", skinColor("#FFFFFF"));
		
		fgColors.put(ServerSettings.WINDOW_MAIN, MAIN_COLOR);
		bgColors.put(ServerSettings.WINDOW_MAIN, MAIN_COLOR);
		
		bgColors.put("roomName", skinColor("#0000FF"));
		bgColors.put("bold", MAIN_COLOR);
		bgColors.put("speech", MAIN_COLOR);
		bgColors.put("whisper", MAIN_COLOR);
		bgColors.put("thought", MAIN_COLOR);
		bgColors.put("watching", MAIN_COLOR);
		bgColors.put("link", MAIN_COLOR);
		bgColors.put("cmdline", skinColor("#000000"));
		bgColors.put("selectedLink", skinColor("#62B0FF"));
		bgColors.put("command", skinColor("#404040"));
		
		commandLineBarColor = skinColor("#FFFFFF");
		
		defaultWindowForeground = skinColor("#F0F0FF");
		defaultWindowBackground = skinColor("191932");
	}
	
	protected StormFrontColor getMainForeground () {
		StormFrontColor mainFG = settings.getMainWindowSettings().getForegroundColor(false);
		mainFG = mainFG.equals(StormFrontColor.DEFAULT_COLOR) ? defaultWindowForeground : mainFG;
		return mainFG;
	}
	
	protected StormFrontColor getMainBackground () {
		StormFrontColor mainBG = settings.getMainWindowSettings().getBackgroundColor(false);
		mainBG = mainBG.equals(StormFrontColor.DEFAULT_COLOR) ? defaultWindowBackground: mainBG;
		return mainBG;
	}
	
	public WarlockColor getColor(ColorType type) {
		if (type == ColorType.MainWindow_Background)
			return getSkinBackgroundColor(settings.getWindowSettings(ServerSettings.WINDOW_MAIN));
		else if (type == ColorType.MainWindow_Foreground)
			return getSkinForegroundColor(settings.getWindowSettings(ServerSettings.WINDOW_MAIN));
		else if (type == ColorType.CommandLine_Background)
			return getSkinBackgroundColor(settings.getCommandLineSettings());
		else if (type == ColorType.CommandLine_Foreground)
			return getSkinForegroundColor(settings.getCommandLineSettings());
		else if (type == ColorType.CommandLine_BarColor)
			return commandLineBarColor;
		
		return StormFrontColor.DEFAULT_COLOR;
	}
	
	public StormFrontColor getStormFrontColor(ColorType type) {
		return (StormFrontColor) getColor(type);
	}

	public String getFontFace(FontFaceType type) {
		if (System.getProperties().getProperty("os.name").indexOf("Windows") != -1)
		{
			return "Verdana";
		}
		return "Sans";
	}

	public int getFontSize(FontSizeType type) {
		return DEFAULT_FONT_SIZE; 
	}
	
	// These are hard coded for now, we should either have our own "skin" defined in a configuration somewhere,
	// or try to pull from stormfront's binary "skn" file somehow?
	// At any rate -- these look to be the right "default" settings for stormfront..
	public StormFrontColor getSkinForegroundColor (ColorSetting setting)
	{
		StormFrontColor color = StormFrontColor.DEFAULT_COLOR;
		
		if (fgColors.containsKey(setting.getId()))
		{
			color = fgColors.get(setting.getId());
		}
		
		if (color == MAIN_COLOR)
		{
			color = getMainForeground();
		}
		
		return color;
	}
	
	public StormFrontColor getSkinBackgroundColor (ColorSetting setting)
	{
		StormFrontColor color = StormFrontColor.DEFAULT_COLOR;
		
		if (bgColors.containsKey(setting.getId()))
		{
			color = bgColors.get(setting.getId());
		}
		
		if (color == MAIN_COLOR)
		{
			color = getMainBackground();
		}
		
		return color;
	}

	protected Preset getPresetForId(ServerSettings settings, String id, boolean fillEntireLine)
	{
		Preset p = new Preset(settings, settings.getPalette());
		p.setName(id);
		p.setForegroundColor(fgColors.get(id));
		p.setBackgroundColor(bgColors.get(id));
		p.setFillEntireLine(fillEntireLine);
		
		return p;
	}
	
	protected void addDefaultPreset(String name, boolean fillEntireLine, ServerSettings settings, Map<String, Preset> presets)
	{
		if (!presets.containsKey(name)) {
			presets.put(name, getPresetForId(settings, name, fillEntireLine));
		}
	}
	
	public void loadDefaultPresets(ServerSettings settings, Map<String, Preset> presets) {
		addDefaultPreset(Preset.PRESET_ROOM_NAME, true, settings, presets);
		addDefaultPreset(Preset.PRESET_BOLD, false, settings, presets);
		addDefaultPreset(Preset.PRESET_COMMAND, false, settings, presets);
		addDefaultPreset(Preset.PRESET_LINK, false, settings, presets);
		addDefaultPreset(Preset.PRESET_SELECTED_LINK, false, settings, presets);
		addDefaultPreset(Preset.PRESET_SPEECH, false, settings, presets);
		addDefaultPreset(Preset.PRESET_THOUGHT, false, settings, presets);
		addDefaultPreset(Preset.PRESET_WATCHING, false, settings, presets);
		addDefaultPreset(Preset.PRESET_WHISPER, false, settings, presets);
	}

	public StormFrontColor getDefaultWindowBackground() {
		return defaultWindowBackground;
	}

	public StormFrontColor getDefaultWindowForeground() {
		return defaultWindowForeground;
	}
}
