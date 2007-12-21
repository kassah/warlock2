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
