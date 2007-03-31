package com.arcaner.warlock.rcp.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.arcaner.warlock.script.IScriptEngine;

/**
 * The main plugin class to be used in the desktop.
 */
public class Warlock2Plugin extends AbstractUIPlugin {
	//The shared instance.
	private static Warlock2Plugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	private ArrayList<IScriptEngine> engines;
	
	/**
	 * The constructor.
	 */
	public Warlock2Plugin() {
		super();
		plugin = this;
		engines = new ArrayList<IScriptEngine>();
		
		try {
			resourceBundle = ResourceBundle.getBundle("com.arcaner.warlock.rcp.plugin.Warlock2PluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		IExtension extensions[] = getExtensions("com.arcaner.warlock.rcp.scriptEngines");
		for (int i = 0; i < extensions.length; i++) {
			IExtension ext = extensions[i];
			IConfigurationElement[] ce = ext.getConfigurationElements();
			
			for (int j = 0; j < ce.length; j++) {
				Object obj = ce[j].createExecutableExtension("classname");
				
				if (obj instanceof IScriptEngine)
				{
					engines.add((IScriptEngine) obj);
				}
			}
		}
	}
	
	public IExtension[] getExtensions (String extensionPoint)
	{
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint(extensionPoint);
		IExtension[] extensions = ep.getExtensions();
		
		return extensions;
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static Warlock2Plugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = Warlock2Plugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	public Collection<IScriptEngine> getScriptEngines ()
	{
		return engines;
	}
}
