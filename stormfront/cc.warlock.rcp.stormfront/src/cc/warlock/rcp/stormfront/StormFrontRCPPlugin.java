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
package cc.warlock.rcp.stormfront;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import cc.warlock.core.profile.Profile;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.javascript.JavascriptEngine;
import cc.warlock.core.stormfront.preferences.StormFrontProfileProvider;
import cc.warlock.core.stormfront.script.javascript.StormFrontJavascriptVars;
import cc.warlock.rcp.application.WarlockApplication;
import cc.warlock.rcp.stormfront.adapters.StormFrontClientAdapterFactory;
import cc.warlock.rcp.stormfront.ui.actions.ProfileConnectAction;
import cc.warlock.rcp.ui.client.WarlockClientAdaptable;

/**
 * The activator class controls the plug-in life cycle
 */
public class StormFrontRCPPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "cc.warlock.rcp.stormfront";

	// The shared instance
	private static StormFrontRCPPlugin plugin;
	
	/**
	 * The constructor
	 */
	public StormFrontRCPPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		JavascriptEngine engine = (JavascriptEngine)ScriptEngineRegistry.getScriptEngine(JavascriptEngine.ENGINE_ID);
		engine.addVariableProvider(new StormFrontJavascriptVars());
		
		Platform.getAdapterManager().registerAdapters(new StormFrontClientAdapterFactory(), WarlockClientAdaptable.class);
		
		String startWithProfile = WarlockApplication.getInstance().getStartWithProfile();
		if (startWithProfile != null) {
			Profile connectToProfile = StormFrontProfileProvider.getInstance().getByName(startWithProfile);
			
			if (connectToProfile == null) {
				/* TODO show a warning */
				return;
			}
			
			ProfileConnectAction action = new ProfileConnectAction(connectToProfile);
			action.run();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
	public static StormFrontRCPPlugin getDefault() {
		return plugin;
	}

}
