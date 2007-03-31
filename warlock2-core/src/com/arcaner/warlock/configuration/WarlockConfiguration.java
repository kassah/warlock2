package com.arcaner.warlock.configuration;

import java.io.File;
import java.io.IOException;

public class WarlockConfiguration {

	public static File getUserHomeDirectory ()
	{
		if (System.getProperty("os.name").contains("Windows"))
		{
			return new File(System.getenv("AppData"));
		}
		
		return new File(System.getProperty("user.home"));
	}
	
	public static File getConfigurationDirectory ()
	{
		if (System.getProperty("os.name").contains("Windows"))
		{
			return new File(getUserHomeDirectory(), "Warlock2");
		}
		
		return new File(getUserHomeDirectory(), ".warlock2"); 
	}
	
	public static File getConfigurationFile (String fileName)
	{
		return getConfigurationFile(fileName, true);
	}
	
	public static File getConfigurationFile (String fileName, boolean lazyCreate)
	{
		File configDir = getConfigurationDirectory();
		
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
