package cc.warlock.rcp.configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.configuration.IConfigurationProvider;
import cc.warlock.core.configuration.WarlockConfiguration;

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
		logFormat = LOG_FORMAT_HTML;
		enableLogging = true;
		logDirectory = ConfigurationUtil.getConfigurationDirectory("logs", false);
		
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
			enableLogging = Boolean.parseBoolean(element.attributeValue("enable"));
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
