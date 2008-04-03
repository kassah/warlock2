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
package cc.warlock.rcp.ui.style;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import cc.warlock.core.configuration.ConfigurationUtil;

public class SavedStyles {

	// TODO - determine if styles should be synchronized
	private static HashMap<String,Style> styles = new HashMap<String,Style>();
	private static Properties props = new Properties();
	
	public static final String STYLE_MAIN_WINDOW = "mainWindow";
	public static final String STYLE_ROOM_NAME = "roomName";
	public static final String STYLE_SPEECH = "speech";
	
	private static final String STYLE_PREFIX = "style.";
	private static final String FOREGROUND = "foreground";
	private static final String BACKGROUND = "background";
	private static final String RED = "red";
	private static final String GREEN = "green";
	private static final String BLUE = "blue";
	private static final String FONT_SIZE = "fontSize";
	private static final String FONT_NAME = "fontName";
	private static final String BOLD = "bold";
	private static final String ITALIC = "italic";
	private static final String UNDERLINE = "underline";
	
	static {
		try {
			File configFile = ConfigurationUtil.getConfigurationFile("styles.properties");
			InputStream stream;
			if (!configFile.exists())
			{
				URL url = SavedStyles.class.getClassLoader().getResource("com/arcaner/warlock/rcp/ui/styles/default-styles.properties");
				stream = url.openStream();
			}
			else stream = new FileInputStream(configFile);	
			
			props.load(stream);
			stream.close();
			for (Object obj : props.keySet())
			{
				String property = (String) obj;
				if (property.startsWith(STYLE_PREFIX))
				{
					String elements[] = property.split("\\.");
					String styleName = elements[1];
					String propertyName = elements[2];
					
					Style style = styles.get(styleName);
					
					if (style == null) {
						style = new Style();
						style.setStyleName(styleName);
						styles.put(styleName, style);
					}
					
					if (FOREGROUND.equals(propertyName) && style.getForeground() == null)
						style.setForeground(getColorFromStyle(styleName, FOREGROUND));
					else if (BACKGROUND.equals(propertyName) && style.getBackground() == null)
						style.setBackground(getColorFromStyle(styleName, BACKGROUND));
					else if (FONT_SIZE.equals(propertyName))
						style.setFontSize(Integer.parseInt(props.getProperty(property)));
					else if (FONT_NAME.equals(propertyName))
						style.setFontName(props.getProperty(property));
					else if (BOLD.equals(propertyName))
						style.setBold(Boolean.parseBoolean(props.getProperty(property)));
					else if (ITALIC.equals(propertyName))
						style.setItalic(Boolean.parseBoolean(props.getProperty(property)));
					else if (UNDERLINE.equals(propertyName))
						style.setUnderline(Boolean.parseBoolean(props.getProperty(property)));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void save ()
	{
		props.clear();
		
		for (Style style : styles.values())
		{
			setColorFromStyle(style.getStyleName(), FOREGROUND, style.getForeground());
			setColorFromStyle(style.getStyleName(), BACKGROUND, style.getBackground());
			setStyleProperty(style.getStyleName(), FONT_SIZE, style.getFontSize() + "");
			setStyleProperty(style.getStyleName(), FONT_NAME, style.getFontName());
			setStyleProperty(style.getStyleName(), BOLD, style.isBold()+"");
			setStyleProperty(style.getStyleName(), ITALIC, style.isItalic()+"");
			setStyleProperty(style.getStyleName(), UNDERLINE, style.isUnderline()+"");
		}
		
		try {
			FileOutputStream out = new FileOutputStream(ConfigurationUtil.getConfigurationFile("styles.properties"));
			
			props.store(out, "Generated by Warlock 2.0");
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Collection<Style> getAllStyles ()
	{
		return styles.values();
	}
	
	public static Style getStyleFromName (String styleName)
	{
		return styles.get(styleName);
	}
	
	public static void addStyle (Style style)
	{
		styles.put(style.getStyleName(), style);
		save();
	}
	
	public static void removeStyle (String styleName)
	{
		styles.remove(styleName);
		save();
	}
	
	private static Color getColorFromStyle (String styleName, String prefix)
	{
		int red = Integer.parseInt(getStyleProperty(styleName, prefix + "." + RED));
		int green = Integer.parseInt(getStyleProperty(styleName, prefix + "." + GREEN));
		int blue = Integer.parseInt(getStyleProperty(styleName, prefix + "." + BLUE));
		
		return new Color(Display.getDefault(), red, green, blue);
	}
	
	private static void setColorFromStyle (String styleName, String prefix, Color color)
	{
		setStyleProperty(styleName, prefix + "." + RED, color.getRed() + "");
		setStyleProperty(styleName, prefix + "." + GREEN, color.getGreen() + "");
		setStyleProperty(styleName, prefix + "." + BLUE, color.getBlue() + "");
	}

	private static String getStyleProperty (String styleName, String propertyName)
	{
		return props.getProperty(STYLE_PREFIX + styleName + "." + propertyName);
	}
	
	private static void setStyleProperty (String styleName, String propertyName, String value)
	{
		props.setProperty(STYLE_PREFIX + styleName + "." + propertyName, value);
	}
}
