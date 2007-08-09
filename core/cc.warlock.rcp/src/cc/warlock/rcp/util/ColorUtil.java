package cc.warlock.rcp.util;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import cc.warlock.client.stormfront.WarlockColor;

public class ColorUtil {

	public static WarlockColor rgbToWarlockColor (RGB rgb)
	{
		return new WarlockColor(rgb.red, rgb.green, rgb.blue);
	}
	
	public static WarlockColor colorToWarlockColor (Color color)
	{
		return rgbToWarlockColor(color.getRGB());
	}
	
	public static RGB warlockColorToRGB (WarlockColor color)
	{
		return new RGB(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	public static Color warlockColorToColor (WarlockColor color)
	{
		return new Color(Display.getDefault(), warlockColorToRGB(color));
	}
}
