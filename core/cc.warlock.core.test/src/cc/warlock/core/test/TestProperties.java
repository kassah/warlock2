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
package cc.warlock.core.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;

import cc.warlock.core.util.ConfigurationUtil;

public class TestProperties {

	protected static Properties testProperties;
	
	public static final String PROFILE_NAMES = "profileNames";
	public static final String DEFAULT_PROFILE = "defaultProfile";
	public static final String DO_CONNECTED_TESTS = "doConnectedTests";
	
	public static Properties getTestProperties ()
	{
		if (testProperties == null)
		{
			testProperties = new Properties();
			
			try {
				FileInputStream stream = new FileInputStream(new File(ConfigurationUtil.getUserHomeDirectory(), "warlock-tests.properties"));
				testProperties.load(stream);
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return testProperties;
	}
	
	public static boolean getBoolean (String property)
	{
		String str = getString(property);
		if (str == null) return false;
		
		return Boolean.parseBoolean(str);
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
	
	public static void failProperty (String property)
	{
		Assert.fail("The property \"" + property + "\" was not defined in " + ConfigurationUtil.getUserHomeDirectory().getAbsolutePath()+ "/warlock-tests.properties");
	}
}
