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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.osgi.framework.Bundle;

import cc.warlock.core.client.ICompass.DirectionType;
import cc.warlock.rcp.plugin.Warlock2Plugin;


public class CompassThemes {

	private static HashMap<String, CompassTheme> themes = new HashMap<String, CompassTheme>();
	
	private static final String THEME_DESCRIPTION = "theme.description";
	private static final String THEME_TITLE = "theme.title";
	private static final String IMAGE_PREFIX = "image.";
	private static final String POSITION_PREFIX = "position.";
	private static final String MAIN_IMAGE = IMAGE_PREFIX + "compass.main";
	private static final String THEME_PATH = "/themes/compass/";
	
	static {
		Bundle pluginBundle = Warlock2Plugin.getDefault().getBundle();
		Enumeration<URL> paths = pluginBundle.findEntries("/themes/compass", "theme.properties", true);
		
		while (paths.hasMoreElements())
		{
			URL url = paths.nextElement();
			String path = url.getPath();
			int slashIndex = path.indexOf('/', THEME_PATH.length());
			String themeName = path.substring(THEME_PATH.length(), slashIndex);
			
			try {
				Properties themeProperties = new Properties();
				InputStream stream = url.openStream();
				themeProperties.load(stream);
				stream.close();
				
				loadThemeProperties(themeName, themeProperties);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void init () { }
	
	private static void loadThemeProperties (String themeName, Properties themeProperties)
	{
		CompassTheme theme = new CompassTheme();
		theme.setName(themeName);
		theme.setDescription(themeProperties.getProperty(THEME_DESCRIPTION));
		theme.setTitle(themeProperties.getProperty(THEME_TITLE));
		theme.setMainImage(THEME_PATH + themeName + "/" +  themeProperties.getProperty(MAIN_IMAGE));
		
		for (DirectionType direction : DirectionType.values())
		{
			if (direction != DirectionType.None)
			{
				theme.setDirectionImage(direction, THEME_PATH + themeName + "/" + themeProperties.getProperty(IMAGE_PREFIX + direction.getName()));
				theme.setDirectionPosition(direction, themeProperties.getProperty(POSITION_PREFIX + direction.getName()));
			}
		}
		
		themes.put(theme.getName(), theme);
	}
	
	public static CompassTheme getCompassTheme (String themeName)
	{
		return themes.get(themeName);
	}
}
