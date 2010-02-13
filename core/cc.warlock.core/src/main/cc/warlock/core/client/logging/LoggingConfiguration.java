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
package cc.warlock.core.client.logging;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cc.warlock.core.configuration.IConfigurationProvider;
import cc.warlock.core.configuration.WarlockConfiguration;
import cc.warlock.core.util.ConfigurationUtil;


public class LoggingConfiguration implements IConfigurationProvider {

	public static final String LOG_FORMAT_TEXT = "text";
	public static final String LOG_FORMAT_HTML = "html";
	
	protected String logFormat;
	protected boolean enableLogging;
	protected File logDirectory;
	
	protected static LoggingConfiguration _instance;
	
	public static LoggingConfiguration instance()
	{
		if (_instance == null) _instance = new LoggingConfiguration();
		return _instance;
	}
	
	protected LoggingConfiguration ()
	{
		logFormat = LOG_FORMAT_TEXT;
		enableLogging = true;
		logDirectory = ConfigurationUtil.getConfigurationDirectory("logs", true);
		
		WarlockConfiguration.getMainConfiguration().addConfigurationProvider(this);
	}
	
	public List<Element> getTopLevelElements() {
		Element logging = DocumentHelper.createElement("logging");
		logging.addAttribute("enabled", enableLogging+"");
		logging.addAttribute("format", logFormat);
		
		Element dir = DocumentHelper.createElement("dir");
		logging.add(dir);
		dir.setText(logDirectory.getAbsolutePath());
		
		return Arrays.asList(new Element[] { logging });
	}

	public void parseElement(Element element) {
		if (element.getName().equals("logging"))
		{
			enableLogging = Boolean.parseBoolean(element.attributeValue("enabled"));
			logFormat = element.attributeValue("format");
			
			Element dir = element.element("dir");
			if (dir != null)
			{
				logDirectory = new File(dir.getTextTrim());
			}
		}
	}

	public boolean supportsElement(Element element) {
		if (element.getName().equals("logging"))
		{
			return true;
		}
		return false;
	}

	public String getLogFormat() {
		return logFormat;
	}

	public void setLogFormat(String logFormat) {
		this.logFormat = logFormat;
	}

	public boolean isLoggingEnabled() {
		return enableLogging;
	}

	public void setLoggingEnabled(boolean enableLogging) {
		this.enableLogging = enableLogging;
	}

	public File getLogDirectory() {
		return logDirectory;
	}

	public void setLogDirectory(File logDirectory) {
		this.logDirectory = logDirectory;
	}

}
