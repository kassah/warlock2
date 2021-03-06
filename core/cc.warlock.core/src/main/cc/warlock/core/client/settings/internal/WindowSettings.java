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

import cc.warlock.core.client.WarlockFont;
import cc.warlock.core.client.settings.IWindowSettings;
import cc.warlock.core.client.settings.IWindowSettingsProvider;

public class WindowSettings extends ColorFontSetting implements IWindowSettings {

	protected String id;
	protected WarlockFont columnFont = WarlockFont.DEFAULT_FONT;
	
	public WindowSettings(IWindowSettingsProvider provider) {
		super(provider);
	}
	
	public WindowSettings (WindowSettings other)
	{
		super(other);
		
		this.id = other.id == null ? null : new String(other.id);
		this.columnFont = other.columnFont == null ? null : new WarlockFont(other.columnFont);
	}
	
	public WarlockFont getColumnFont() {
		return columnFont;
	}
	
	public void setColumnFont(WarlockFont columnFont) {
		if (!columnFont.equals(this.columnFont))
			needsUpdate = true;
		
		this.columnFont = columnFont;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		if (!id.equals(this.id))
			needsUpdate = true;
		
		this.id = id;
	}
	
	public WindowSettings getOriginalWindowSettings ()
	{
		return (WindowSettings) originalSetting;
	}
	
	@Override
	public String toString() {
		return "[" + id + "]";
	}
}
