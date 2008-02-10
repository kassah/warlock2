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

import cc.warlock.core.client.settings.IClientSettingProvider;
import cc.warlock.core.client.settings.IColorSetting;
import cc.warlock.core.stormfront.client.StormFrontColor;
import cc.warlock.core.stormfront.xml.StormFrontElement;

@Deprecated
public abstract class ColorSetting extends ServerSetting implements Comparable<ColorSetting>, IColorSetting {

	protected String foregroundColor, backgroundColor;
	protected Palette palette;
	protected String foregroundKey = KEY_FGCOLOR;
	protected boolean needsUpdate;
	
	public static final String KEY_FGCOLOR = "color";
	public static final String KEY_BGCOLOR = "bgcolor";
	
	public ColorSetting (ServerSettings serverSettings, Palette palette)
	{
		super(serverSettings);
		
		this.palette = palette;
	}
	
	public ColorSetting (ServerSettings settings, StormFrontElement element, Palette palette)
	{
		super(settings, element);
		
		this.palette = palette;
	}
	
	public abstract String getId ();
	
	public StormFrontColor getDefaultForegroundColor ()
	{
		if (this == serverSettings.getMainWindowSettings())
		{
			return new StormFrontColor(serverSettings.getDefaultSkin().getDefaultWindowForeground());
		}
		else
		{
			return serverSettings.getMainWindowSettings().getForegroundColor();
		}
	}
	
	public StormFrontColor getDefaultBackgroundColor ()
	{
		if (this == serverSettings.getMainWindowSettings())
		{
			return new StormFrontColor(serverSettings.getDefaultSkin().getDefaultWindowBackground());
		}
		else
		{
			return serverSettings.getMainWindowSettings().getBackgroundColor();
		}
	}
	
	public StormFrontColor getForegroundColor() {
		return getForegroundColor(true);
	}
	
	public StormFrontColor getForegroundColor (boolean skinFallback)
	{
		return getColorFromString(foregroundKey, foregroundColor, skinFallback);
	}

	public StormFrontColor getBackgroundColor() {
		return getBackgroundColor(true);
	}
	
	public StormFrontColor getBackgroundColor (boolean skinFallback)
	{
		return getColorFromString(KEY_BGCOLOR, backgroundColor, skinFallback);
	}
	
	protected StormFrontColor getColorFromString (String key, String color, boolean skinFallback)
	{
		if (color == null || color.length() == 0)
		{
			if (KEY_FGCOLOR.equals(key) || foregroundKey.equals(key))
				return getDefaultForegroundColor();
			else if (KEY_BGCOLOR.equals(key))
				return getDefaultBackgroundColor();
			else return StormFrontColor.DEFAULT_COLOR;
		}
		else if (color.charAt(0) == '@')
		{
			StormFrontColor paletteColor = palette.getPaletteColor(color.substring(1));
			
			return paletteColor;
		}
		else if ("skin".equals(color))
		{
			if (!skinFallback)
				return StormFrontColor.DEFAULT_COLOR;
			
			if (KEY_FGCOLOR.equals(key) || foregroundKey.equals(key))
				return new StormFrontColor(serverSettings.getDefaultSkin().getSkinForegroundColor(getId(), this));
			else if (KEY_BGCOLOR.equals(key))
				return new StormFrontColor(serverSettings.getDefaultSkin().getSkinBackgroundColor(getId(), this));
		}
		
		return new StormFrontColor(color);
	}

	protected String assignColor (StormFrontColor color, String currentColor)
	{
		palette.setPaletteColor(color.getPaletteId(), color);
		String stormfrontString = color.toStormfrontString();
		
		if (!stormfrontString.equals(currentColor))
			needsUpdate = true;
		
		return stormfrontString;
	}
	
	public Palette getPalette() {
		return palette;
	}

	public void setPalette(Palette palette) {
		this.palette = palette;
	}

	public void setForegroundColor(StormFrontColor foregroundColor) {
		this.foregroundColor = assignColor(foregroundColor, this.foregroundColor);
	}
	
	public void setBackgroundColor(StormFrontColor backgroundColor) {
		this.backgroundColor = assignColor(backgroundColor, this.backgroundColor);
	}
	
	public void setDefaultForegroundColor ()
	{
		this.foregroundColor = null;
	}
	
	public void setSkinForegroundColor ()
	{
		this.foregroundColor = "skin";
	}
	
	public void setDefaultBackgroundColor ()
	{
		if (this.foregroundColor != null)
			needsUpdate = true;
		this.foregroundColor = null;
	}
	
	public void setSkinBackgroundColor ()
	{
		if (!"skin".equals(this.backgroundColor))
			needsUpdate = true;
			
		this.backgroundColor = "skin";
	}

	public boolean needsUpdate() {
		return needsUpdate;
	}
	
	public void setNeedsUpdate (boolean needsUpdate)
	{
		this.needsUpdate = needsUpdate;
	}
	
	public int compareTo(ColorSetting o) {
		return foregroundColor.compareTo(o.foregroundColor);
	}
	
	public IClientSettingProvider getProvider() {
		return null;
	}
	
}
