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
