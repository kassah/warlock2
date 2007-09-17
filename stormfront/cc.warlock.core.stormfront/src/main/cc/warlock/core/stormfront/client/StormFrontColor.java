package cc.warlock.core.stormfront.client;

import java.util.TreeSet;

import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.stormfront.serversettings.server.Palette;

/**
 * This class is intended to mimic SWT's RGB class so we can maintain separation.
 * @author Marshall
 */
public class StormFrontColor extends WarlockColor {

	public static final StormFrontColor DEFAULT_COLOR = new StormFrontColor(-1, -1, -1);
	
	private boolean skinColor;
	private String paletteId;
	
	// this will help us with re-capturing "dead" palette colors
	private TreeSet<Object> paletteReferences = new TreeSet<Object>();
	
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
