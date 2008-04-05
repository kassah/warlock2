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
/*
 * Created on Dec 30, 2004
 */
package cc.warlock.rcp.application;

import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;

import cc.warlock.core.configuration.WarlockConfiguration;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

/**
 * @author Marshall
 */
public class WarlockApplication implements IApplication, IAdaptable {
	private String startWithProfile = null;
	private static WarlockApplication _instance;
	private WarlockWorkbenchAdvisor advisor;
	
	public WarlockApplication ()
	{
		_instance = this;
	}
	
	public static WarlockApplication instance()
	{
		return _instance;
	}
	
	private void parseArguments (String[] arguments)
	{
		JSAP jsap = new JSAP();
		FlaggedOption profileOption = new FlaggedOption("profile",
			JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.NOT_REQUIRED, 'p', "profile", "The profile to start Warlock with");
		
		try {
			jsap.registerParameter(profileOption);
		} catch (JSAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSAPResult result = jsap.parse(arguments);
		if (result.contains("profile"))
		{
			startWithProfile = result.getString("profile");
		}
	}
	
	public Object start(IApplicationContext context) throws Exception {
		Map args = context.getArguments();
		String arguments[] = (String[]) args.get(IApplicationContext.APPLICATION_ARGS);
		
		parseArguments(arguments);
		
		Display display = PlatformUI.createDisplay();
		advisor = new WarlockWorkbenchAdvisor();
		int ret = PlatformUI.createAndRunWorkbench(display, advisor);
		
		//save configuration
		WarlockConfiguration.saveAll();
		
		if (ret == PlatformUI.RETURN_RESTART)
			return EXIT_RESTART;
		
		return EXIT_OK;
	}
	
	public void stop() {
		
	}

	public String getStartWithProfile() {
		return startWithProfile;
	}
	
	// This exists solely so that ScriptsWindowHandler can configure the
	// workbench window it creates.
	public IWorkbenchWindowConfigurer getWindowConfigurer(IWorkbenchWindow w) {
		return advisor.getWindowConfigurer(w);
	}

	// Generic getAdapter because this is used as a model for the CNF-based
	// script view.
	public Object getAdapter(Class adapter) {
		if (adapter.isInstance(this))
			return this;
		else
			return null;
	}
}
