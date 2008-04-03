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
package cc.warlock.rcp.util;

import java.util.HashMap;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.WarlockColor;

public class ColorUtil {

	// cached associations for minimizing object overhead
	protected static HashMap<WarlockColor, Color> cachedColors = new HashMap<WarlockColor, Color>();
	protected static HashMap<WarlockColor, RGB> cachedRGBs = new HashMap<WarlockColor, RGB>();
	protected static HashMap<RGB, WarlockColor> cachedRGBWarlockColors = new HashMap<RGB,WarlockColor>();
	
	public static WarlockColor rgbToWarlockColor (RGB rgb)
	{
		if (!cachedRGBWarlockColors.containsKey(rgb))
		{
			WarlockColor color = new WarlockColor(rgb.red, rgb.green, rgb.blue);
			
			cachedRGBWarlockColors.put(rgb, color);
			cachedRGBs.put(color, rgb);
		}
		return cachedRGBWarlockColors.get(rgb);
	}
	
	public static WarlockColor colorToWarlockColor (Color color)
	{
		
		return rgbToWarlockColor(color.getRGB());
	}
	
	public static RGB warlockColorToRGB (WarlockColor color)
	{
		if (!cachedRGBs.containsKey(color))
		{
			RGB rgb = new RGB(color.getRed(), color.getGreen(), color.getBlue());
			
			cachedRGBWarlockColors.put(rgb, color);
			cachedRGBs.put(color, rgb);
		}
		return cachedRGBs.get(color);
	}
	
	public static Color warlockColorToColor (WarlockColor color)
	{
		if (!cachedColors.containsKey(color))
		{
			Color c2 = new Color(Display.getDefault(), warlockColorToRGB(color));
			
			cachedColors.put(color, c2);
		}
		return cachedColors.get(color);
	}
}
