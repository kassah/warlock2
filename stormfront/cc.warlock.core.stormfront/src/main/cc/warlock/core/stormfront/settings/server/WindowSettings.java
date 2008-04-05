/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.core.stormfront.settings.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cc.warlock.core.stormfront.xml.StormFrontAttribute;
import cc.warlock.core.stormfront.xml.StormFrontElement;

@Deprecated
public class WindowSettings extends ColorSetting {

	protected StormFrontElement windowElement;
	
	protected HashMap<String, String> windowAttributes = new HashMap<String, String>();
	protected HashMap<String, String> fontAttributes = new HashMap<String, String>();
	protected HashMap<String, String> columnFontAttributes = new HashMap<String, String>();
	
	protected String id;
	protected String fontFace, columnFontFace;
	protected int fontSize, columnFontSize;
	
	public static final int EMPTY_FONT_SIZE = -1;
	public static final String EMPTY_FONT_FACE = "";
	
	protected static final String KEY_FGCOLOR = "fgcolor";
	protected static final String KEY_FACE = "face";
	protected static final String KEY_SIZE = "size";
	protected static ArrayList<String> skipAttributes = new ArrayList<String>();
	static {
		Collections.addAll(skipAttributes, KEY_FGCOLOR, KEY_BGCOLOR, KEY_FACE, KEY_SIZE);
	}
	
	protected static final String STORMFRONT_MARKUP_PREFIX = "<stream>";
	protected static final String STORMFRONT_MARKUP_SUFFIX = "</stream>";
	
	protected boolean needsUpdate = false;
	protected WindowSettings originalWindowSettings;
	
	public WindowSettings (ServerSettings settings, StormFrontElement windowElement, Palette palette)
	{
		super(settings, windowElement, palette);
		this.foregroundKey = KEY_FGCOLOR;
		
		id = windowElement.attributeValue("id");
		fontFace = columnFontFace = EMPTY_FONT_FACE;
		fontSize = columnFontSize = EMPTY_FONT_SIZE;
	
		this.windowElement = windowElement;
		addAttributes(windowElement, windowAttributes);
		
		foregroundColor = windowElement.attributeValue(KEY_FGCOLOR);
		backgroundColor = windowElement.attributeValue(KEY_BGCOLOR);
		
		
		StormFrontElement fontElement = windowElement.element("font");
		StormFrontElement columnFontElement = windowElement.element("columnFont");
		
		if (fontElement != null)
		{
			fontFace = fontElement.attributeValue(KEY_FACE);
			String size = fontElement.attributeValue(KEY_SIZE);
			if(size != null)
				fontSize = Integer.parseInt(size);
			addAttributes(fontElement, fontAttributes);
		}
		if (columnFontElement != null)
		{
			columnFontFace = columnFontElement.attributeValue(KEY_FACE);
			String size = columnFontElement.attributeValue(KEY_SIZE);
			if(size != null)
				columnFontSize = Integer.parseInt(size);
			addAttributes(columnFontElement, columnFontAttributes);
		}
	}
	
	public WindowSettings (WindowSettings other)
	{
		super(other.serverSettings, new StormFrontElement(other.element, true), other.palette);
		this.foregroundKey = KEY_FGCOLOR;
		this.windowElement = this.element;
		this.windowAttributes.putAll(other.windowAttributes);
		this.fontAttributes.putAll(other.fontAttributes);
		this.columnFontAttributes.putAll(other.columnFontAttributes);
		
		this.id = other.id == null ? null : new String(other.id);
		this.foregroundColor = other.foregroundColor == null ? null : new String(other.foregroundColor);
		this.backgroundColor = other.backgroundColor == null ? null : new String(other.backgroundColor);
		
		this.fontFace = other.fontFace == null ? null : new String(other.fontFace);
		this.columnFontFace = other.columnFontFace == null ? null : new String(other.columnFontFace);
		this.fontSize = other.fontSize;
		this.columnFontSize= other.columnFontSize;
		
		this.originalWindowSettings = other;
	}
	
	protected void addAttributes (StormFrontElement element, HashMap<String,String> map)
	{
		for (StormFrontAttribute attribute : element.attributes())
		{
			if (!skipAttributes.contains(attribute.getName()))
			{
				map.put(attribute.getName(), attribute.getValue());
			}
		}
	}
	
	@Override
	protected void saveToDOM() {
		StormFrontElement parent = originalWindowSettings.element.getParent();
		
		parent.removeElement(originalWindowSettings.element);
		parent.addElement(element);
		
		setAttribute(KEY_FGCOLOR, foregroundColor);
		setAttribute(KEY_BGCOLOR, backgroundColor);
		
		StormFrontElement fontElement = element.element("font");
		StormFrontElement columnFontElement = element.element("columnFont");
		
		if (fontElement == null && (fontFace != EMPTY_FONT_FACE || fontSize != EMPTY_FONT_SIZE))
		{
			fontElement = new StormFrontElement("font");
			element.addElement(fontElement);
		}
		
		if (columnFontElement == null && (columnFontFace != EMPTY_FONT_FACE || columnFontSize != EMPTY_FONT_SIZE))
		{
			columnFontElement = new StormFrontElement("columnFont");
			element.addElement(columnFontElement);
		}
		
		if (fontElement != null)
		{
			if (fontFace != EMPTY_FONT_FACE)
				setAttribute(fontElement, KEY_FACE, fontFace);
			
			if (fontSize != EMPTY_FONT_SIZE)
				setAttribute(fontElement, KEY_SIZE, ""+fontSize);	
		}
		
		if (columnFontElement != null)
		{
			if (columnFontFace != EMPTY_FONT_FACE)
				setAttribute(columnFontElement, KEY_FACE, columnFontFace);
			
			if (columnFontSize != EMPTY_FONT_SIZE)
				setAttribute(columnFontElement, KEY_SIZE, ""+columnFontSize);	
		}
	}

	protected void appendAttributes(StringBuffer markup, Map<String,String> attributes)
	{
		//Sorting will ensure they maintain the same order when being saved
		ArrayList<String> attributeNames = new ArrayList<String>();
		attributeNames.addAll(attributes.keySet());
		Collections.sort(attributeNames);
		
		for (String attribute : attributeNames)
		{
			markup.append(attribute + "=\"" + attributes.get(attribute) + "\" ");
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
		return windowElement.toXML("", false, includeFonts);
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

	public int getFontSizeInPixels() {
		return fontSize;
	}
	
	public int getFontSizeInPoints() {
		return getPixelSizeInPoints(fontSize);
	}

	public void setFontSizeInPixels(int fontSize) {
		if (fontSize != this.fontSize)
			needsUpdate = true;
		
		this.fontSize = fontSize;
	}
	
	public void setFontSizeInPoints(int fontSize) {
		setFontSizeInPixels(getPointSizeInPixels(fontSize));
	}

	public int getColumnFontSizeInPixels() {
		return columnFontSize;
	}
	
	public int getColumnFontSizeInPoints() {
		return getPixelSizeInPoints(columnFontSize);
	}

	public void setColumnFontSizeInPixels(int columnFontSize) {
		if (columnFontSize != this.columnFontSize)
			needsUpdate = true;
		
		this.columnFontSize = columnFontSize;
	}
	
	public void setColumnFontSizeInPoints(int fontSize) {
		setColumnFontSizeInPixels(getPointSizeInPixels(fontSize));
	}

	public WindowSettings getOriginalWindowSettings ()
	{
		return originalWindowSettings;
	}
	
	public void setNeedsUpdate (boolean needsUpdate)
	{
		super.setNeedsUpdate(needsUpdate);
		if (!needsUpdate)
			originalWindowSettings = null;
	}

	public String getId() {
		return id;
	}
	
	public int compareTo(WindowSettings o) {
		return id.compareTo(o.id);
	}
}
