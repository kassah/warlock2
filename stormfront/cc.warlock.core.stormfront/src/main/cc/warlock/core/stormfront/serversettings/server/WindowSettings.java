package cc.warlock.core.stormfront.serversettings.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;

import cc.warlock.core.stormfront.client.StormFrontColor;

public class WindowSettings extends ServerSetting {

	protected Element windowElement;
	
	protected HashMap<String, String> windowAttributes = new HashMap<String, String>();
	protected HashMap<String, String> fontAttributes = new HashMap<String, String>();
	protected HashMap<String, String> columnFontAttributes = new HashMap<String, String>();
	
	protected String id;
	protected String foregroundColor, backgroundColor;
	protected String fontFace, columnFontFace;
	protected int fontSize, columnFontSize;
	
	public static final int EMPTY_FONT_SIZE = -1;
	public static final String EMPTY_FONT_FACE = "";
	
	protected static final String KEY_FGCOLOR = "fgcolor";
	protected static final String KEY_BGCOLOR = "bgcolor";
	protected static final String KEY_FACE = "face";
	protected static final String KEY_SIZE = "size";
	protected static ArrayList<String> skipAttributes = new ArrayList<String>();
	static {
		Collections.addAll(skipAttributes, KEY_FGCOLOR, KEY_BGCOLOR, KEY_FACE, KEY_SIZE);
	}
	
	protected static final String STORMFRONT_MARKUP_PREFIX = "<stream>";
	protected static final String STORMFRONT_MARKUP_SUFFIX = "</stream>";
	
	protected boolean needsUpdate = false;
	protected Palette palette;
	protected WindowSettings originalWindowSettings;
	
	public WindowSettings (ServerSettings settings, Element windowElement, Palette palette)
	{
		super(settings, windowElement);
		
		id = windowElement.attributeValue("id");
		fontFace = columnFontFace = EMPTY_FONT_FACE;
		fontSize = columnFontSize = EMPTY_FONT_SIZE;
	
		this.palette = palette;
		this.windowElement = windowElement;
		addAttributes(windowElement, windowAttributes);
		
		foregroundColor = windowElement.attributeValue(KEY_FGCOLOR);
		backgroundColor = windowElement.attributeValue(KEY_BGCOLOR);
		
		
		Element fontElement = windowElement.element("font");
		Element columnFontElement = windowElement.element("columnFont");
		
		if (fontElement != null)
		{
			fontFace = fontElement.attributeValue(KEY_FACE);
			fontSize = Integer.parseInt(fontElement.attributeValue(KEY_SIZE));
			addAttributes(fontElement, fontAttributes);
		}
		if (columnFontElement != null)
		{
			columnFontFace = columnFontElement.attributeValue(KEY_FACE);
			columnFontSize = Integer.parseInt(columnFontElement.attributeValue(KEY_SIZE));
			addAttributes(columnFontElement, columnFontAttributes);
		}
	}
	
	public WindowSettings (WindowSettings other)
	{
		super(other.serverSettings, other.element);
		this.palette = other.palette;
		
		this.windowAttributes.putAll(other.windowAttributes);
		this.fontAttributes.putAll(other.fontAttributes);
		this.columnFontAttributes.putAll(other.columnFontAttributes);
		
		this.foregroundColor = other.foregroundColor == null ? null : new String(other.foregroundColor);
		this.backgroundColor = other.backgroundColor == null ? null : new String(other.backgroundColor);
		
		this.fontFace = other.fontFace == null ? null : new String(other.fontFace);
		this.columnFontFace = other.columnFontFace == null ? null : new String(other.columnFontFace);
		this.fontSize = other.fontSize;
		this.columnFontSize= other.columnFontSize;
		
		this.originalWindowSettings = other;
	}
	
	protected void addAttributes (Element element, HashMap<String,String> map)
	{
		for (Attribute attribute : (List<Attribute>) element.attributes())
		{
			if (!skipAttributes.contains(attribute.getName()))
			{
				map.put(attribute.getName(), attribute.getValue());
			}
		}
	}
	
	@Override
	protected void saveToDOM() {
		setAttribute(KEY_FGCOLOR, foregroundColor);
		setAttribute(KEY_BGCOLOR, backgroundColor);
		
		Element fontElement = windowElement.element("font");
		Element columnFontElement = windowElement.element("columnFont");
		
		if (fontElement != null)
		{
			if (fontFace != EMPTY_FONT_FACE)
				setAttribute(fontElement, KEY_FACE, fontFace);
			
			if (fontSize != EMPTY_FONT_SIZE)
				setAttribute(fontElement, KEY_SIZE, ""+fontSize);	
		}
		
		if (columnFontElement != null)
		{
			if (fontFace != EMPTY_FONT_FACE)
				setAttribute(columnFontElement, KEY_FACE, columnFontFace);
			
			if (fontSize != EMPTY_FONT_SIZE)
				setAttribute(columnFontElement, KEY_SIZE, ""+columnFontSize);	
		}
	}

	protected void appendAttributes(StringBuffer markup, Map<String,String> attributes)
	{
		for (Map.Entry<String,String> entry : attributes.entrySet())
		{
			markup.append(entry.getKey() + "=\"" + entry.getValue() + "\" ");
		}
	}
	
	protected void appendFont (StringBuffer markup, String tagName, Map<String,String> attributes, String fontFace, int fontSize)
	{
		if (fontFace != EMPTY_FONT_FACE || fontSize != EMPTY_FONT_SIZE)
		{
			markup.append("<" + tagName + " ");
			appendAttributes(markup, attributes);
		
			if (fontFace != EMPTY_FONT_FACE)
			{
				markup.append(KEY_FACE + "=\"" + fontFace + "\" ");
			}
			
			if (fontSize != EMPTY_FONT_SIZE)
			{
				markup.append(KEY_SIZE + "=\"" + fontSize + "\" ");
			}
			markup.append("/>");
		}
	}
	
	@Override
	protected String toStormfrontMarkup() {
		return toStormfrontMarkup(true);
	}
	
	public String toStormfrontMarkup(boolean includeFonts)
	{
		StringBuffer markup = new StringBuffer();
		
		markup.append("<w ");
		appendAttributes(markup, windowAttributes);
		
		markup.append(KEY_FGCOLOR + "=\"" + foregroundColor + "\" ");
		markup.append(KEY_BGCOLOR + "=\"" + foregroundColor + "\" ");
		markup.append(">");
		
		if (includeFonts)
		{
			appendFont (markup, "font", fontAttributes, fontFace, fontSize);
			appendFont (markup, "columnFont", columnFontAttributes, columnFontFace, columnFontSize);
		}
			
		markup.append("</w>");
		return markup.toString();
	}

	public String getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(StormFrontColor foregroundColor) {
		foregroundColor.assignToPalette(palette);
		String stormfrontString = foregroundColor.toStormfrontString();
		
		if (!stormfrontString.equals(this.foregroundColor))
			needsUpdate = true;
		
		this.foregroundColor = stormfrontString;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(StormFrontColor backgroundColor) {
		backgroundColor.assignToPalette(palette);
		String stormfrontString = backgroundColor.toStormfrontString();
		
		if (!stormfrontString.equals(this.backgroundColor))
			needsUpdate = true;
		
		this.backgroundColor = stormfrontString;
	}

	public String getFontFace() {
		return fontFace;
	}

	public void setFontFace(String fontFace) {
		if (! this.fontFace.equals(fontFace))
			needsUpdate = true;
		
		this.fontFace = fontFace;
	}

	public String getColumnFontFace() {
		return columnFontFace;
	}

	public void setColumnFontFace(String columnFontFace) {
		if (! this.columnFontFace.equals(columnFontFace))
			needsUpdate = true;
		
		this.columnFontFace = columnFontFace;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		if (fontSize != this.fontSize)
			needsUpdate = true;
		
		this.fontSize = fontSize;
	}

	public int getColumnFontSize() {
		return columnFontSize;
	}

	public void setColumnFontSize(int columnFontSize) {
		if (columnFontSize != this.columnFontSize)
			needsUpdate = true;
		
		this.columnFontSize = columnFontSize;
	}

	public WindowSettings getOriginalWindowSettings ()
	{
		return originalWindowSettings;
	}
	
	public boolean needsUpdate()
	{
		return needsUpdate;
	}
	
	public void setNeedsUpdate (boolean needsUpdate)
	{
		this.needsUpdate = needsUpdate;
	}

	public String getId() {
		return id;
	}
}
