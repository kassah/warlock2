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

import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.client.WarlockFont;
import cc.warlock.core.client.settings.IClientSettingProvider;
import cc.warlock.core.client.settings.IColorSetting;
import cc.warlock.core.client.settings.IFontSetting;

/**
 * @author marshall
 *
 */
public abstract class ColorFontSetting extends ClientSetting implements IColorSetting, IFontSetting {

	protected WarlockColor foregroundColor = WarlockColor.DEFAULT_COLOR, backgroundColor = WarlockColor.DEFAULT_COLOR;
	protected WarlockFont font = WarlockFont.DEFAULT_FONT;
	
	public ColorFontSetting (ColorFontSetting other)
	{
		super(other);
		
		this.foregroundColor = new WarlockColor(other.foregroundColor);
		this.backgroundColor = new WarlockColor(other.backgroundColor);
		this.font = new WarlockFont(other.font);
	}
	
	public ColorFontSetting (IClientSettingProvider provider)
	{
		super(provider);
	}

	public WarlockColor getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(WarlockColor foregroundColor) {
		if (!foregroundColor.equals(this.foregroundColor)) 
			needsUpdate = true;
		
		this.foregroundColor = foregroundColor;
	}

	public WarlockColor getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(WarlockColor backgroundColor) {
		if (!backgroundColor.equals(this.backgroundColor)) 
			needsUpdate = true;
		
		this.backgroundColor = backgroundColor;
	}

	public WarlockFont getFont() {
		return font;
	}

	public void setFont(WarlockFont font) {
		if (!font.equals(this.font)) 
			needsUpdate = true;
		
		this.font = font;
	}
	
	
}
