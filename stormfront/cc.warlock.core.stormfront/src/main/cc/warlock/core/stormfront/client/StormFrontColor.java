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
package cc.warlock.core.stormfront.client;

import cc.warlock.core.client.WarlockColor;

/**
 * This class is intended to mimic SWT's RGB class so we can maintain separation.
 * @author Marshall
 */
public class StormFrontColor extends WarlockColor {

	public static final StormFrontColor DEFAULT_COLOR = new StormFrontColor(-1, -1, -1);
	
	private boolean skinColor;
	private String paletteId;
	
	public StormFrontColor () { super(); }
	public StormFrontColor (int red, int green, int blue)
	{
		super(red, green, blue);
	}
	public StormFrontColor (String colorString)
	{
		super(colorString);
	}
	
	public StormFrontColor (WarlockColor other)
	{
		super(other.getRed(), other.getGreen(), other.getBlue());
	}
	
	public String toStormfrontString ()
	{
		if (isSkinColor()) return "skin";
		if (getPaletteId() != null) return "@" + getPaletteId();
		return toHexString();
	}
	
	public boolean isSkinColor() {
		return skinColor;
	}
	public void setSkinColor(boolean skinColor) {
		this.skinColor = skinColor;
	}
	public String getPaletteId() {
		return paletteId;
	}
	public void setPaletteId(String paletteId) {
		this.paletteId = paletteId;
	}
}
