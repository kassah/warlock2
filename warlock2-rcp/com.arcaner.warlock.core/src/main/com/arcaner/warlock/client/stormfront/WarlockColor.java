package com.arcaner.warlock.client.stormfront;

/**
 * This class is intended to mimic SWT's RGB class so we can maintain separation
 * @author Marshall
 *
 */
public class WarlockColor {

	public static final WarlockColor DEFAULT_COLOR = new WarlockColor(-1, -1, -1);
	
	private int red, green, blue;

	public WarlockColor () { }
	public WarlockColor (int red, int green, int blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public WarlockColor (String hexColor)
	{
		if (hexColor.charAt(0) == '#')
		{
			hexColor = hexColor.substring(1);
		}
		
		red = Integer.parseInt(hexColor.substring(0, 2), 16);
		green = Integer.parseInt(hexColor.substring(2, 4), 16);
		blue = Integer.parseInt(hexColor.substring(4, 6), 16);
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
	
}
