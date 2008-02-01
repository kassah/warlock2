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
package cc.warlock.core.script.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.internal.Property;
import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.configuration.IConfigurationProvider;
import cc.warlock.core.script.javascript.JavascriptEngine;

public class ScriptConfiguration implements IConfigurationProvider {

	protected TreeSet<File> scriptDirectories = new TreeSet<File>();
	protected Property<Boolean> autoScan;
	protected Property<Long> scanTimeout;
	protected String scriptPrefix;
	
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
		scriptPrefix = ".";
		
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
		scriptPrefix = ".";
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
		
		Element scriptPrefixElement = DocumentHelper.createElement("script-prefix");
		scriptPrefixElement.setText(scriptPrefix);
		scriptConfig.add(scriptPrefixElement);
		
		addEngineExtensionsConfig(scriptConfig);
	}
	
	public void addEngineExtension (String engineId, String extension)
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
		
		// If we already have the extension, don't add it
		if(!extensions.contains(extension))
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
		// clear();
		
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
			else if ("script-prefix".equals(element.getName()))
			{
				scriptPrefix = element.getTextTrim();
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
		if (engineExtensions.containsKey(engineId))
			return engineExtensions.get(engineId);
		return Collections.emptyList();
	}
	
	public Set<File> getScriptDirectories ()
	{
		return scriptDirectories;
	}

	public String getScriptPrefix() {
		return scriptPrefix;
	}

	public void setScriptPrefix(String scriptPrefix) {
		this.scriptPrefix = scriptPrefix;
	}
}
