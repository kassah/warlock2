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
package cc.warlock.core.configuration;

import java.io.File;
import java.io.IOException;

public class ConfigurationUtil {

	public static final String MAIN_CONFIGURATION_FILE = "config.xml";
	
	public static File getAppConfigDirectory ()
	{
		if (System.getProperty("os.name").contains("Windows"))
		{
			return new File(System.getenv("AppData"), "Warlock2");
		}
		else {
			return new File(getUserHomeDirectory(), ".warlock2");
		}
	}
	
	public static File getUserHomeDirectory ()
	{	
		return new File(System.getProperty("user.home"));
	}
	
	public static File getConfigurationDirectory (String directory, boolean lazyCreate)
	{
		File dirFile = new File(getAppConfigDirectory(), directory);
		
		if (lazyCreate && !dirFile.exists())
		{
			dirFile.mkdirs();
		}
		return dirFile;
	}
	
	public static File getUserDirectory (String directory, boolean lazyCreate)
	{
		File dirFile = new File(getUserHomeDirectory(), directory);
		
		if (lazyCreate && !dirFile.exists())
		{
			dirFile.mkdirs();
		}
		return dirFile;
	}
	
	public static File getConfigurationFile (String fileName)
	{
		return getConfigurationFile(fileName, true);
	}
	
	public static File getConfigurationFile (String fileName, boolean lazyCreate)
	{
		File configDir = getAppConfigDirectory();
		
		if (!configDir.exists())
		{
			configDir.mkdirs();
		}
		
		File configFile = new File(configDir, fileName);
		
		if (!configFile.exists() && lazyCreate)
		{
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return configFile;
	}
}
