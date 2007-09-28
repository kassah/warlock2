package cc.warlock.core.script.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.internal.Property;
import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.configuration.IConfigurationProvider;
import cc.warlock.core.script.javascript.JavascriptEngine;

public class ScriptConfiguration implements IConfigurationProvider {

	protected ArrayList<File> scriptDirectories = new ArrayList<File>();
	protected Property<Boolean> autoScan;
	protected Property<Long> scanTimeout;
	
	protected HashMap<String, ArrayList<String>> engineExtensions = new HashMap<String, ArrayList<String>>();
	
	protected static ScriptConfiguration _instance;
	
	public static ScriptConfiguration instance()
	{
		if (_instance == null)
			_instance = new ScriptConfiguration();
		return _instance;
	}
	
	protected ScriptConfiguration ()
	{
		autoScan = new Property<Boolean>("autoScan", true);
		scanTimeout = new Property<Long>("scanTimeout", (long)500);
		
		addEngineExtension(JavascriptEngine.ENGINE_ID, "js");
		
		scriptDirectories.add(ConfigurationUtil.getUserDirectory("scripts", false));
		scriptDirectories.add(ConfigurationUtil.getUserDirectory("warlock-scripts", false));
		scriptDirectories.add(ConfigurationUtil.getConfigurationDirectory("scripts", false));
	}
	
	protected void clear ()
	{
		scriptDirectories.clear();
		autoScan.set(true);
		scanTimeout.set((long)500);
		engineExtensions.clear();
	}
	
	public List<Element> getTopLevelElements() {
		ArrayList<Element> elements = new ArrayList<Element>();
		
		Element scriptConfig = DocumentHelper.createElement("script-config");
		elements.add(scriptConfig);
		
		addScriptConfigElements(scriptConfig);
		
		return elements;
	}
	
	protected void addScriptConfigElements (Element scriptConfig)
	{
		for (File dir : scriptDirectories)
		{
			Element dirElement = DocumentHelper.createElement("dir");
			dirElement.setText(dir.getAbsolutePath());
			
			scriptConfig.add(dirElement);
		}
		
		Element autoScanElement = DocumentHelper.createElement("autoScan");
		autoScanElement.addAttribute("timeout", ""+scanTimeout.get());
		autoScanElement.setText(autoScan.get()+"");
		scriptConfig.add(autoScanElement);
		
		addEngineExtensionsConfig(scriptConfig);
	}
	
	protected void addEngineExtension (String engineId, String extension)
	{
		ArrayList<String> extensions;
		if (engineExtensions.containsKey(engineId))
		{
			extensions = engineExtensions.get(engineId);
		}
		else
		{
			extensions = new ArrayList<String>();
			engineExtensions.put(engineId, extensions);
		}
		
		extensions.add(extension);
	}
	
	protected void addEngineExtensionsConfig (Element scriptConfig)
	{
		for (Map.Entry<String, ArrayList<String>> entry : engineExtensions.entrySet())
		{
			Element engineElement = DocumentHelper.createElement("engine-file-extensions");
			engineElement.addAttribute("engineId", entry.getKey());
			scriptConfig.add(engineElement);
			
			for (String extension : entry.getValue())
			{
				Element extElement = DocumentHelper.createElement("extension");
				extElement.setText(extension);
				engineElement.add(extElement);
			}
		}
	}

	public void parseElement(Element element) {
		if (element.getName().equals("script-config"))
		{
			parseScriptConfig(element);
		}
	}
	
	public void parseScriptConfig (Element scriptConfig)
	{
		clear();
		
		for (Element element : (List<Element>)scriptConfig.elements())
		{
			if ("dir".equals(element.getName()))
			{
				scriptDirectories.add(new File(element.getTextTrim()));
			}
			else if ("autoScan".equals(element.getName()))
			{
				scanTimeout.set(Long.parseLong(element.attributeValue("timeout")));
				autoScan.set(Boolean.parseBoolean(element.getTextTrim()));
			}
			else if ("engine-file-extensions".equals(element.getName()))
			{
				for (Element extElement : (List<Element>)element.elements())
				{
					addEngineExtension(element.attributeValue("engineId"), extElement.getTextTrim());
				}
			}
		}
	}

	public boolean supportsElement(Element element) {
		if (element.getName().equals("script-config"))
		{
			return true;
		}
		return false;
	}

	public IProperty<Boolean> getAutoScan() {
		return autoScan;
	}

	public IProperty<Long> getScanTimeout() {
		return scanTimeout;
	}

	public List<String> getEngineExtensions (String engineId)
	{
		return engineExtensions.get(engineId);
	}
	
	public List<File> getScriptDirectories ()
	{
		return scriptDirectories;
	}
}
