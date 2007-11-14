package cc.warlock.core.stormfront.serversettings.server;

import java.util.regex.Pattern;

import cc.warlock.core.client.IHighlightString;
import cc.warlock.core.stormfront.xml.StormFrontElement;

public class HighlightPreset extends Preset implements IHighlightString {

	public static final String KEY_TEXT = "text";
	
	public static final String STRINGS_PREFIX = "<strings>";
	public static final String STRINGS_SUFFIX = "</strings>";
	public static final String NAMES_PREFIX = "<names>";
	public static final String NAMES_SUFFIX = "</names>";
	
	protected String text;
	protected boolean isName, isNew = false;
	protected HighlightPreset originalString;
	protected Pattern pattern;
	
	protected HighlightPreset (ServerSettings serverSettings, Palette palette)
	{
		super(serverSettings, palette);
	}
	
	public HighlightPreset (HighlightPreset other)
	{
		super(other);
		
		this.text = other.text == null ? null : new String(other.text);
		this.isName = other.isName;
		this.isNew = other.isNew;
		this.originalString = other;
	}
	
	public HighlightPreset (ServerSettings serverSettings, StormFrontElement highlightElement, Palette palette)
	{
		super(serverSettings, highlightElement, palette);
		
		this.text = highlightElement.attributeValue("text");
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (!text.equals(this.text))
			needsUpdate = true;
		
		this.text = text;
		this.pattern = null;
	}
	
	public boolean isName() {
		return isName;
	}

	public void setIsName(boolean isName) {
		this.isName = isName;
	}

	protected void saveToDOM ()
	{
		setAttribute(KEY_FGCOLOR, foregroundColor);
		setAttribute(KEY_BGCOLOR, backgroundColor == null ? "" : backgroundColor);
		setAttribute(KEY_TEXT, getText());
		
		if (fillEntireLine)
			setAttribute(KEY_FILL_ENTIRE_LINE, "y");
	}
	
	protected String toStormfrontMarkup ()
	{
		return "<h text=\"" + getText() + "\" " +
			KEY_FGCOLOR + "=\"" + foregroundColor + "\" " +
			KEY_BGCOLOR + "=\"" + (backgroundColor == null ? "" : backgroundColor) + "\"" +
			(fillEntireLine ? (" " + KEY_FILL_ENTIRE_LINE + "=\"y\"/>") : "/>");
	}
	
	protected String toStormfrontAddMarkup ()
	{
		String aPrefix = (isName ? NAMES_PREFIX : STRINGS_PREFIX) + ADD_PREFIX;
		String aSuffix = ADD_SUFFIX + (isName ? NAMES_SUFFIX : STRINGS_SUFFIX);	
		String mPrefix = (isName ? NAMES_PREFIX : STRINGS_PREFIX) + UPDATE_PREFIX;
		String mSuffix = UPDATE_SUFFIX + (isName ? NAMES_SUFFIX : STRINGS_SUFFIX);
		
		String emptyTag = "<h";
		String justTextTag = emptyTag + " text=\"" + getText() + "\"";
		String textAndColorTag = justTextTag + " " + KEY_FGCOLOR + "=\"" + foregroundColor + "\"";
		String textAndColorsTag = textAndColorTag + " " + KEY_BGCOLOR + "=\"" + (backgroundColor == null ? "" : backgroundColor) + "\"";
		String allAttribsTag = textAndColorsTag + (fillEntireLine ? (" " + KEY_FILL_ENTIRE_LINE + "=\"y\"/>") : "/>");
		
		emptyTag += "/>"; justTextTag += "/>"; textAndColorTag += "/>"; textAndColorsTag += "/>";
		
		return 
			aPrefix + emptyTag + aSuffix +
			mPrefix + emptyTag + justTextTag + mSuffix +
			mPrefix + justTextTag + textAndColorTag + mSuffix +
			mPrefix + textAndColorTag + textAndColorsTag + mSuffix +
			(fillEntireLine ?
				mPrefix + textAndColorsTag + allAttribsTag + mSuffix : "");
	}
	
	public static HighlightPreset createHighlightStringFromParent (ServerSettings serverSettings, StormFrontElement parent)
	{
		StormFrontElement element = new StormFrontElement("h");
		parent.addElement(element);
		
		HighlightPreset string = new HighlightPreset(serverSettings, element, serverSettings.getPalette());
		string.needsUpdate = true;
		return string;
	}
	
	@Override
	public int compareTo(ColorSetting o) {
		if (o instanceof HighlightPreset)
		{
			HighlightPreset other = (HighlightPreset) o;
			return text.compareTo(other.text);
		}
		else return -1;
	}
	
	@Override
	public void setNeedsUpdate(boolean needsUpdate) {
		super.setNeedsUpdate(needsUpdate);
		if (!needsUpdate)
			this.originalString = null;
	}
	
	public HighlightPreset getOriginalHighlightString ()
	{
		return originalString;
	}
	
	@Override
	public String toString() {
		return (isName ? "name: " : "string: ") + text;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	public Pattern getPattern() {
		if(pattern == null)
			pattern = Pattern.compile(text);
		
		return pattern;
	}
}