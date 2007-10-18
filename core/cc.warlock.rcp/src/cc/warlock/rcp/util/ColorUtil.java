package cc.warlock.rcp.util;

import java.util.Hashtable;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import cc.warlock.core.client.WarlockColor;

public class ColorUtil {

	// cached associations for minimizing object overhead
	protected static Hashtable<WarlockColor,Color> cachedColors = new Hashtable<WarlockColor, Color>();
	protected static Hashtable<WarlockColor,RGB> cachedRGBs = new Hashtable<WarlockColor, RGB>();
	protected static Hashtable<RGB,WarlockColor> cachedRGBWarlockColors = new Hashtable<RGB,WarlockColor>();
	
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
