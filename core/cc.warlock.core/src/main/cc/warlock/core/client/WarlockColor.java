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
package cc.warlock.core.client;


/**
 * This class is intended to mimic SWT's RGB class so we can maintain separation.
 * @author Marshall
 */
public class WarlockColor {

	public static final WarlockColor DEFAULT_COLOR = new WarlockColor(-1, -1, -1);
	
	protected int red, green, blue = -1;
	
	public WarlockColor () { }
	
	public WarlockColor (WarlockColor other)
	{
		this.red = other.red;
		this.green = other.green;
		this.blue = other.blue;
	}
	
	public WarlockColor (int red, int green, int blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public WarlockColor (String colorString)
	{
		if (colorString.charAt(0) == '#')
		{
			colorString = colorString.substring(1);
		}
		
		red = Integer.parseInt(colorString.substring(0, 2), 16);
		green = Integer.parseInt(colorString.substring(2, 4), 16);
		blue = Integer.parseInt(colorString.substring(4, 6), 16);
	}
	
	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}
	
	private String padHexString (String hex)
	{
		if (hex == null || hex.length() == 0) return "00";
		else if (hex.length() == 1) return "0" + hex;
		else return hex;
	}
	
	public String toHexString () {
		return "#" + padHexString(Integer.toHexString(red).toUpperCase()) +
			"" + padHexString(Integer.toHexString(green).toUpperCase()) +
			"" + padHexString(Integer.toHexString(blue).toUpperCase());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WarlockColor)
		{
			WarlockColor other = (WarlockColor)obj;
			
			return (red == other.getRed() && green == other.getGreen() && blue == other.getBlue());
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return toHexString() + " (r: " + red + ", g: " + green + ", b: " + blue +")";
	}
	
	public boolean isDefault ()
	{
		return this.equals(DEFAULT_COLOR);
	}
}
