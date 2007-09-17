package cc.warlock.rcp.plugin;

import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.rcp.ui.macros.MacroRegistry;

/**
 * The main plugin class to be used in the desktop.
 */
public class Warlock2Plugin extends AbstractUIPlugin {
	//The shared instance.
	private static Warlock2Plugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	private ArrayList<IWarlockClient> clients = new ArrayList<IWarlockClient>();
	
	public static final String PLUGIN_ID = "cc.warlock.rcp";
	
	/**
	 * The constructor.
	 */
	public Warlock2Plugin() {
		super();
		plugin = this;
		
		try {
			resourceBundle = ResourceBundle.getBundle("cc.warlock.rcp.plugin.Warlock2PluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		// force load of the Macro registry
		MacroRegistry.instance();
	}
	
	public IWarlockClient getCurrentClient ()
	{
		return clients.get(clients.size()-1);
	}
	
	public void addClient (IWarlockClient client)
	{
		clients.add(client);
	}
	
	public IWarlockClient addNextClient (IWarlockClient client)
	{
		IWarlockClient oldClient = getCurrentClient();
		
		clients.add(client);
		return oldClient;
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
}
