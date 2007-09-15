package cc.warlock.client.stormfront;

import java.util.TreeSet;

import cc.warlock.configuration.server.Palette;

/**
 * This class is intended to mimic SWT's RGB class so we can maintain separation.
 * @author Marshall
 */
public class WarlockColor {

	public static final WarlockColor DEFAULT_COLOR = new WarlockColor(-1, -1, -1);
	
	private int red, green, blue;
	private boolean skinColor;
	private String paletteId;
	
	// this will help us with re-capturing "dead" palette colors
	private TreeSet<Object> paletteReferences = new TreeSet<Object>();
	
	public WarlockColor () { }
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
	
	public String toStormfrontString ()
	{
		if (isSkinColor()) return "skin";
		if (getPaletteId() != null) return "@" + getPaletteId();
		return toHexString();
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
	
	public String assignToPalette (Palette palette)
	{
		if (paletteId != null)
		{
			// We already have a palette id
			return paletteId;
		}

		String existingId = palette.findColor(this);
		if (existingId != null)
		{
			// This color is already defined on the palette, so just reference that paletteId
			this.paletteId = existingId;
			return paletteId;
		}
		
		// just allocate a new color in the palette
		String firstFreePaletteId = palette.getFirstFreePaletteId();
		if (firstFreePaletteId!= null)
		{
			palette.setPaletteColor(firstFreePaletteId, this);
			this.paletteId = firstFreePaletteId;
			return this.paletteId;
		}
		
		return null;
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WarlockColor)
		{
			WarlockColor other = (WarlockColor)obj;
			
			return (red == other.getRed() && green == other.getGreen() && blue == other.getBlue());
		}
		return super.equals(obj);
	}
	
	public int hashCode() {
		assert false : "hashCode not designed";
		return 42;
	}
	
	public int getPaletteReferenceCount ()
	{
		return paletteReferences.size();
	}
	
	public void addPaletteReference(Object referrer)
	{
		paletteReferences.add(referrer);
	}
	
	public void removePaletteReference(Object referrer)
	{
		paletteReferences.remove(referrer);
	}
}
