package cc.warlock.rcp.ui.style;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.eclipse.swt.graphics.Point;
import org.osgi.framework.Bundle;

import cc.warlock.client.ICompass.DirectionType;
import cc.warlock.rcp.plugin.Warlock2Plugin;


public class CompassThemes {

	private static Hashtable<String, CompassTheme> themes = new Hashtable<String, CompassTheme>();
	
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
