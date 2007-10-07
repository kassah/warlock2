package cc.warlock.core.stormfront.serversettings.server;

import cc.warlock.core.stormfront.xml.StormFrontElement;

public class Preset extends ColorSetting {

	public static final String PRESET_BOLD = "bold";
	public static final String PRESET_ROOM_NAME = "roomName";
	public static final String PRESET_SPEECH = "speech";
	public static final String PRESET_WHISPER = "whisper";
	public static final String PRESET_THOUGHT = "thought";
	public static final String PRESET_WATCHING = "watching";
	public static final String PRESET_LINK = "link";
	public static final String PRESET_SELECTED_LINK = "selectedLink";
	public static final String PRESET_COMMAND = "command";
	
	public static final String KEY_FILL_ENTIRE_LINE = "line";
	
	public static final String STORMFRONT_MARKUP_PREFIX = "<presets>";
	public static final String STORMFRONT_MARKUP_SUFFIX = "</presets>";
	
	protected boolean needsUpdate = false, fillEntireLine;
	protected String name;
	protected Preset originalPreset;
		
	public Preset (ServerSettings serverSettings, Palette palette)
	{
		super(serverSettings, palette);
	}
		
	public Preset (Preset other)
	{
		super(other.serverSettings, other.element, other.palette);
		this.name = other.name == null ? null : new String(other.name);
		this.foregroundColor = other.foregroundColor == null ? null : new String(other.foregroundColor);
		this.backgroundColor = other.backgroundColor == null ? null : new String(other.backgroundColor);
		this.fillEntireLine = other.fillEntireLine;
		
		this.originalPreset = other;
	}

	public Preset (ServerSettings serverSettings, StormFrontElement presetElement, Palette palette)
	{
		super(serverSettings, presetElement, palette);
		this.name = presetElement.attributeValue("id");
		
		this.foregroundColor = presetElement.attributeValue(KEY_FGCOLOR);
		this.backgroundColor = presetElement.attributeValue(KEY_BGCOLOR);
		this.fillEntireLine = presetElement.attribute("line") != null ?
				"y".equalsIgnoreCase(presetElement.attributeValue("line")) : false;
	}
	
	public static Preset createPresetFromParent (ServerSettings serverSettings, StormFrontElement parent)
	{
		StormFrontElement element = new StormFrontElement("p");
		parent.addElement(element);
		
		Preset preset = new Preset(serverSettings, element, serverSettings.getPalette());
		return preset;
	}
	
	public boolean isFillEntireLine() {
		return fillEntireLine;
	}
	
	public void setFillEntireLine(boolean fillEntireLine) {
		if (this.fillEntireLine != fillEntireLine)
			needsUpdate = true;
		
		this.fillEntireLine = fillEntireLine;
	}
	
	public String getId() {
		return name;
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

	public int compareTo(ColorSetting o) {
		if (o instanceof Preset)
		{
			Preset p = (Preset) o;
			return name.compareTo(p.name);
		}
		return super.compareTo(o);
	}

	public void setNeedsUpdate(boolean needsUpdate) {
		super.setNeedsUpdate(needsUpdate);
		
		if (!needsUpdate)
			this.originalPreset = null;
	}
	
	public Preset getOriginalPreset ()
	{
		return originalPreset;
	}
}
