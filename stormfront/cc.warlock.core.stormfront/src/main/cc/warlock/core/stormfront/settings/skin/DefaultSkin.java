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
package cc.warlock.core.stormfront.settings.skin;

import java.util.HashMap;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.client.settings.IHighlightProvider;
import cc.warlock.core.client.settings.IHighlightString;
import cc.warlock.core.stormfront.settings.IStormFrontClientSettings;
import cc.warlock.core.stormfront.settings.internal.StormFrontClientSettings;

/**
 * The default skin handles any attributes who's values are "skin"
 * @author marshall
 */
public class DefaultSkin implements IStormFrontSkin {

	public static final int DEFAULT_FONT_SIZE = 12;
	public static final WarlockColor MAIN_COLOR = new WarlockColor(-1, -1, -1);
	
	// TODO give these proper size/load since we know their final sizes.
	protected HashMap<String, WarlockColor> fgColors = new HashMap<String, WarlockColor>();
	protected HashMap<String, WarlockColor> bgColors = new HashMap<String, WarlockColor>();
	protected WarlockColor commandLineBarColor;
	protected IStormFrontClientSettings settings;
	
	protected WarlockColor defaultWindowBackground, defaultWindowForeground;
		
	public DefaultSkin (IStormFrontClientSettings settings)
	{
		this.settings = settings;
		
		fgColors.put("bold", new WarlockColor("#FFFF00"));
		fgColors.put("roomName", new WarlockColor("#FFFFFF"));
		fgColors.put("speech", new WarlockColor("#80FF80"));
		fgColors.put("thought", new WarlockColor("#FF8000"));
		fgColors.put("cmdline", new WarlockColor("#FFFFFF"));
		fgColors.put("whisper", new WarlockColor("#80FFFF"));
		fgColors.put("watching", new WarlockColor("#FFFF00"));
		fgColors.put("link", new WarlockColor("#62B0FF"));
		fgColors.put("selectedLink", new WarlockColor("#000000"));
		fgColors.put("command", new WarlockColor("#FFFFFF"));
		
		fgColors.put(StormFrontClientSettings.WINDOW_MAIN, MAIN_COLOR);
		bgColors.put(StormFrontClientSettings.WINDOW_MAIN, MAIN_COLOR);
		
		bgColors.put("roomName", new WarlockColor("#0000FF"));
		bgColors.put("bold", MAIN_COLOR);
		bgColors.put("speech", MAIN_COLOR);
		bgColors.put("whisper", MAIN_COLOR);
		bgColors.put("thought", MAIN_COLOR);
		bgColors.put("watching", MAIN_COLOR);
		bgColors.put("link", MAIN_COLOR);
		bgColors.put("cmdline", new WarlockColor("#000000"));
		bgColors.put("selectedLink", new WarlockColor("#62B0FF"));
		bgColors.put("command", new WarlockColor("#404040"));
		
		commandLineBarColor = new WarlockColor("#FFFFFF");
		
		defaultWindowForeground = new WarlockColor("#F0F0FF");
		defaultWindowBackground = new WarlockColor("191932");
	}
	
	public WarlockColor getMainForeground () {
		WarlockColor mainFG = settings.getMainWindowSettings().getForegroundColor();
		mainFG = mainFG.isDefault() ? defaultWindowForeground : mainFG;
		return mainFG;
	}
	
	public WarlockColor getMainBackground () {
		WarlockColor mainBG = settings.getMainWindowSettings().getBackgroundColor();
		mainBG = mainBG.isDefault() ? defaultWindowBackground: mainBG;
		return mainBG;
	}
	
	public WarlockColor getColor(ColorType type) {
		if (type == ColorType.MainWindow_Background)
			return getDefaultBackgroundColor(StormFrontClientSettings.WINDOW_MAIN);
		else if (type == ColorType.MainWindow_Foreground)
			return getDefaultForegroundColor(StormFrontClientSettings.WINDOW_MAIN);
		else if (type == ColorType.CommandLine_Background)
			return getDefaultBackgroundColor("command");
		else if (type == ColorType.CommandLine_Foreground)
			return getDefaultForegroundColor("command");
		else if (type == ColorType.CommandLine_BarColor)
			return commandLineBarColor;
		
		return new WarlockColor(WarlockColor.DEFAULT_COLOR);
	}
	
	public WarlockColor getStormFrontColor(ColorType type) {
		return (WarlockColor) getColor(type);
	}

	public String getFontFace(FontFaceType type) {
		if (System.getProperties().getProperty("os.name").contains("Windows"))
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
	public WarlockColor getDefaultForegroundColor (String styleName)
	{
		WarlockColor color = new WarlockColor(WarlockColor.DEFAULT_COLOR);
		
		if (fgColors.containsKey(styleName))
		{
			color = fgColors.get(styleName);
		}
		
		if (color == MAIN_COLOR)
		{
			color = getMainForeground();
		}
		
		return color;
	}
	
	public WarlockColor getDefaultBackgroundColor (String styleName)
	{
		WarlockColor color = new WarlockColor(WarlockColor.DEFAULT_COLOR);
		
		if (bgColors.containsKey(styleName))
		{
			color = bgColors.get(styleName);
		}
		
		if (color == MAIN_COLOR)
		{
			color = getMainBackground();
		}
		
		return color;
	}

	protected IWarlockStyle getStyleForId(String id, boolean fillEntireLine)
	{
		WarlockStyle style = new WarlockStyle();
		style.setForegroundColor(fgColors.get(id));
		style.setBackgroundColor(bgColors.get(id));
		style.setFullLine(fillEntireLine);
		style.setName(id);
		return style;
	}
	
	protected void addDefaultNamedStyle(String name, boolean fillEntireLine, IHighlightProvider provider)
	{
		if (!provider.getHighlightStrings().contains(name)) {
			provider.addNamedStyle(name, getStyleForId(name, fillEntireLine));
		}
	}
	
	public void loadDefaultStyles (IHighlightProvider provider) {
		addDefaultNamedStyle(StormFrontClientSettings.PRESET_ROOM_NAME, true, provider);
		addDefaultNamedStyle(StormFrontClientSettings.PRESET_BOLD, false, provider);
		addDefaultNamedStyle(StormFrontClientSettings.PRESET_COMMAND, false, provider);
		addDefaultNamedStyle(StormFrontClientSettings.PRESET_LINK, false, provider);
		addDefaultNamedStyle(StormFrontClientSettings.PRESET_SELECTED_LINK, false, provider);
		addDefaultNamedStyle(StormFrontClientSettings.PRESET_SPEECH, false, provider);
		addDefaultNamedStyle(StormFrontClientSettings.PRESET_THOUGHT, false, provider);
		addDefaultNamedStyle(StormFrontClientSettings.PRESET_WATCHING, false, provider);
		addDefaultNamedStyle(StormFrontClientSettings.PRESET_WHISPER, false, provider);
	}
	
	public WarlockColor getDefaultWindowBackground() {
		return defaultWindowBackground;
	}

	public WarlockColor getDefaultWindowForeground() {
		return defaultWindowForeground;
	}
	
	public WarlockColor getBackgroundColor(IHighlightString string) {
		WarlockColor background = string.getStyle().getBackgroundColor();
		if (background.isDefault()) {
			background = getMainBackground();
		}
		return background;
	}
	
	public WarlockColor getForegroundColor(IHighlightString string) {
		WarlockColor foreground = string.getStyle().getForegroundColor();
		if (foreground.isDefault()) {
			foreground = getMainForeground();
		}
		return foreground;
	}
}
