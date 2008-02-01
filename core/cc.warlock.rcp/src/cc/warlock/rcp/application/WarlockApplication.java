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
import java.util.Timer;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;

import cc.warlock.core.configuration.WarlockConfiguration;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;

/**
 * @author Marshall
 */
public class WarlockApplication extends WorkbenchAdvisor implements IApplication, IAdaptable {
	
	private String startWithProfile = null;
	private static WarlockApplication _instance;
	private Timer timer = new Timer();
	private boolean showMenus = true;
	private String windowTitle = null;
	private Point initialSize = null;
	private boolean showCoolBar = false;
	
	public WarlockApplication ()
	{
		_instance = this;
	}
	
	public static WarlockApplication instance()
	{
		return _instance;
	}
	
	public String getInitialWindowPerspectiveId ()
	{
		return WarlockPerspectiveFactory.WARLOCK_PERSPECTIVE_ID;
	}
	
	public void preWindowOpen(IWorkbenchWindowConfigurer configurer)
	{
		configurer.setShowPerspectiveBar(false);
		configurer.setShowProgressIndicator(false);
		configurer.setShowFastViewBars(false);
		configurer.setShowCoolBar(showCoolBar);
		configurer.setShowStatusLine(false);
		configurer.setShowMenuBar(showMenus);
		if (windowTitle != null)
			configurer.setTitle(windowTitle);
		if (initialSize != null)
			configurer.setInitialSize(initialSize);
	}
	
	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		configurer.setSaveAndRestore(true);
	}
	
	@Override
	public IAdaptable getDefaultPageInput() {
		return this;
	}
	
	public Object getAdapter(Class adapter) {
		if (adapter.equals(getClass()))
		{
			return this;
		}
		return null;
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
	
	@Override
	public void preStartup() {
		WarlockConfiguration.getMainConfiguration().addConfigurationProvider(WarlockPerspectiveLayout.instance());
	}
	
	@Override
	public void postStartup() {
		WarlockPerspectiveLayout.instance().loadBounds();
	}
	
	@Override
	public boolean preShutdown() {
		timer.cancel();
		
		WarlockPerspectiveLayout.instance().saveLayout();
		return true;
	}
	
	public Object start(IApplicationContext context) throws Exception {
		Map args = context.getArguments();
		String arguments[] = (String[]) args.get(IApplicationContext.APPLICATION_ARGS);
		
		parseArguments(arguments);
		
		Display display = PlatformUI.createDisplay();
		int ret = PlatformUI.createAndRunWorkbench(display, this);
		
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
	
	public Timer getTimer ()
	{
		return timer;
	}

	public void setShowMenus(boolean showMenus) {
		this.showMenus = showMenus;
	}

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}

	public void setInitialSize(Point initialSize) {
		this.initialSize = initialSize;
	}
	
	public void setShowCoolBar(boolean showCoolBar) {
		this.showCoolBar = showCoolBar;
	}
}
