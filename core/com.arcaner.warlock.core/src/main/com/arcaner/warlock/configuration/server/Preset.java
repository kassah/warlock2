package com.arcaner.warlock.configuration.server;

import org.dom4j.Element;

import com.arcaner.warlock.client.stormfront.WarlockColor;
import com.arcaner.warlock.configuration.skin.IWarlockSkin;

public class Preset extends ServerSetting implements Comparable<Preset> {

	public static final String PRESET_ROOM_NAME = "roomName";
	public static final String PRESET_SPEECH = "speech";
	public static final String PRESET_WHISPER = "whisper";
	public static final String PRESET_THOUGHT = "thought";
	public static final String PRESET_WATCHING = "watching";
	public static final String PRESET_LINK = "link";
	public static final String PRESET_SELECTED_LINK = "selectedLink";
	public static final String PRESET_COMMAND = "command";
	
	public static final String KEY_FGCOLOR = "color";
	public static final String KEY_BGCOLOR = "bgcolor";
	public static final String KEY_FILL_ENTIRE_LINE = "line";
	
	public static final String STORMFRONT_MARKUP_PREFIX = "<presets><<m>";
	public static final String STORMFRONT_MARKUP_SUFFIX = "</<m></presets>";
	
	protected String foregroundColor, backgroundColor;
	protected boolean needsUpdate = false, fillEntireLine;
	protected String name;
	protected Palette palette;
	protected Preset originalPreset;
	
	public WarlockColor getDefaultForegroundColor ()
	{
		return serverSettings.getColorSetting(IWarlockSkin.ColorType.MainWindow_Foreground);
	}
	
	public WarlockColor getDefaultBackgroundColor ()
	{
		return serverSettings.getColorSetting(IWarlockSkin.ColorType.MainWindow_Background);
	}
	
	protected WarlockColor getColorFromString (String key, String color)
	{
		if (color == null || color.length() == 0)
		{
			if (KEY_FGCOLOR.equals(key))
				return getDefaultForegroundColor();
			else if (KEY_BGCOLOR.equals(key))
				return getDefaultBackgroundColor();
		}
		else if (color.charAt(0) == '@')
		{
			WarlockColor paletteColor = palette.getPaletteColor(color.substring(1));
			paletteColor.addPaletteReference(this);
			
			return paletteColor;
		}
		else if ("skin".equals(color))
		{
			if (KEY_FGCOLOR.equals(key))
				return serverSettings.getDefaultSkin().getSkinForegroundColor(getName());
			else if (KEY_BGCOLOR.equals(key))
				return serverSettings.getDefaultSkin().getSkinBackgroundColor(getName());
		}
		
		return new WarlockColor(color);
	}
	
	protected Preset (ServerSettings serverSettings)
	{
		super(serverSettings);
	}
	
	public Preset (Preset other)
	{
		super(other.serverSettings, other.element);
		this.palette = other.palette;
		this.name = other.name == null ? null : new String(other.name);
		this.foregroundColor = other.foregroundColor == null ? null : new String(other.foregroundColor);
		this.backgroundColor = other.backgroundColor == null ? null : new String(other.backgroundColor);
		this.fillEntireLine = other.fillEntireLine;
		
		this.originalPreset = other;
	}

	public Preset (ServerSettings serverSettings, Element presetElement, Palette palette)
	{
		super(serverSettings, presetElement);
		this.palette = palette;
		this.name = presetElement.attributeValue("id");
		
		this.foregroundColor = presetElement.attributeValue(KEY_FGCOLOR);
		this.backgroundColor = presetElement.attributeValue(KEY_BGCOLOR);
		this.fillEntireLine = presetElement.attribute("line") != null ?
				"y".equalsIgnoreCase(presetElement.attributeValue("line")) : false;
	}
	
	public static Preset createPresetFromParent (ServerSettings serverSettings, Element parent)
	{
		Element element = parent.addElement("p");
		Preset preset = new Preset(serverSettings, element, serverSettings.getPalette());
		return preset;
	}
	
	public WarlockColor getForegroundColor() {
		return getColorFromString(KEY_FGCOLOR, foregroundColor);
	}
	
	public void setDefaultForegroundColor ()
	{
		this.foregroundColor = null;
	}
	
	public void setSkinForegroundColor ()
	{
		this.foregroundColor = "skin";
	}
	
	public void setForegroundColor(WarlockColor foregroundColor) {
		foregroundColor.assignToPalette(palette);
		String stormfrontString = foregroundColor.toStormfrontString();
		
		if (!stormfrontString.equals(this.foregroundColor))
			needsUpdate = true;
		
		this.foregroundColor = stormfrontString;
	}
	
	public WarlockColor getBackgroundColor() {
		return getColorFromString(KEY_BGCOLOR, backgroundColor);
	}
	
	public void setDefaultBackgroundColor ()
	{
		if (this.foregroundColor != null)
			needsUpdate = true;
		this.foregroundColor = null;
	}
	
	public void setSkinBackgroundColor ()
	{
		if (!"skin".equals(this.backgroundColor))
			needsUpdate = true;
			
		this.backgroundColor = "skin";
	}
	
	public void setBackgroundColor(WarlockColor backgroundColor) {
		backgroundColor.assignToPalette(palette);
		
		String stormfrontString = backgroundColor.toStormfrontString();
		
		if (!stormfrontString.equals(this.backgroundColor))
			needsUpdate = true;
		
		this.backgroundColor = stormfrontString;
	}
	
	public boolean isFillEntireLine() {
		return fillEntireLine;
	}
	
	public void setFillEntireLine(boolean fillEntireLine) {
		if (this.fillEntireLine != fillEntireLine)
			needsUpdate = true;
		
		this.fillEntireLine = fillEntireLine;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if (!name.equals(this.name))
			needsUpdate = true;
		
		this.name = name;
	}
	
	protected void saveToDOM ()
	{
		setAttribute(KEY_FGCOLOR, foregroundColor);
		setAttribute(KEY_BGCOLOR, backgroundColor);
		if (fillEntireLine)
			setAttribute(KEY_FILL_ENTIRE_LINE, "y");
	}
	
	protected String toStormfrontMarkup ()
	{
		return "<p id=\"" + getName() + "\" " +
			KEY_FGCOLOR + "=\"" + foregroundColor + "\" "  +
			KEY_BGCOLOR + "=\"" + backgroundColor + "\"" +
			(fillEntireLine ? (" " + KEY_FILL_ENTIRE_LINE + "=\"y\"/>") : "/>");
	}

	public int compareTo(Preset o) {
		return name.compareTo(o.name);
	}

	public boolean needsUpdate() {
		return needsUpdate;
	}

	public void setNeedsUpdate(boolean needsUpdate) {
		this.needsUpdate = needsUpdate;
		if (!needsUpdate)
			this.originalPreset = null;
	}
	
	public Preset getOriginalPreset ()
	{
		return originalPreset;
	}
}
