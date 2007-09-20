package cc.warlock.core.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import cc.warlock.core.configuration.WarlockConfiguration;

public class TestProperties {

	protected static Properties testProperties;
	
	public static final String PROFILE_NAMES = "profileNames";
	
	public static Properties getTestProperties ()
	{
		if (testProperties == null)
		{
			testProperties = new Properties();
			
			try {
				FileInputStream stream = new FileInputStream(new File(WarlockConfiguration.getUserHomeDirectory(), "warlock-tests.properties"));
				testProperties.load(stream);
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return testProperties;
	}
	
	public static String getString(String property)
	{
		return getTestProperties().getProperty(property);
	}
	
	public static List<String> getList(String property)
	{
		String value = getTestProperties().getProperty(property);
		
		if (value != null) {
			return Arrays.asList(value.split(","));
		}
		
		return null;
	}
}
