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
package cc.warlock.core.script;

import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.ScriptEngineRegistry;

/**
 * The activator class controls the plug-in life cycle
 */
public class WarlockCoreScriptPlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "cc.warlock.core.script";

	// The shared instance
	private static WarlockCoreScriptPlugin plugin;
	
	/**
	 * The constructor
	 */
	public WarlockCoreScriptPlugin() {
	}

	public IExtension[] getExtensions (String extensionPoint)
	{
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint(extensionPoint);
		IExtension[] extensions = ep.getExtensions();
		
		return extensions;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		Platform.getExtensionRegistry().addRegistryChangeListener(
				new IRegistryChangeListener() {
					public void registryChanged(IRegistryChangeEvent event) {
						IExtensionDelta deltas[] = event.getExtensionDeltas("cc.warlock.core.script.scriptEngines");
						
						if (deltas.length > 0)
						{
							for (IExtensionDelta delta : deltas) {
								processDelta(delta);
							}
						}
					}
				}
		);
		
		IExtension extensions[] = getExtensions("cc.warlock.core.script.scriptEngines");
		for (int i = 0; i < extensions.length; i++) {
			IExtension ext = extensions[i];
			addScriptEngines(ext);
		}
		
		plugin = this;
	}

	protected void addScriptEngines (IExtension extension)
	{
		try {
			IConfigurationElement[] ce = extension.getConfigurationElements();
			
			for (int j = 0; j < ce.length; j++) {
				Object obj = ce[j].createExecutableExtension("classname");
				
				if (obj instanceof IScriptEngine)
				{
					ScriptEngineRegistry.addScriptEngine((IScriptEngine)obj);
				}
			}
		} catch (InvalidRegistryObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void removeScriptEngines (IExtension extension)
	{
		IConfigurationElement[] ce = extension.getConfigurationElements();
		
		for (int j = 0; j < ce.length; j++) {
			String classname = ce[j].getAttribute("classname");
			for (IScriptEngine engine : ScriptEngineRegistry.getScriptEngines())
			{
				if (engine.getClass().getName().equals(classname))
				{
					ScriptEngineRegistry.removeScriptEngine(engine);
				}
			}
		}
	}
	
	protected void processDelta (IExtensionDelta delta)
	{
		if (delta.getKind() == IExtensionDelta.ADDED)
		{
			addScriptEngines(delta.getExtension());
		}
		else if (delta.getKind() == IExtensionDelta.REMOVED)
		{
			removeScriptEngines(delta.getExtension());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static WarlockCoreScriptPlugin getDefault() {
		return plugin;
	}

	public Collection<IScriptEngine> getScriptEngines ()
	{
		return ScriptEngineRegistry.getScriptEngines();
	}
}
