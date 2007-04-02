/*
 * Created on Dec 30, 2004
 */
package com.arcaner.warlock.rcp.application;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;

/**
 * @author Marshall
 */
public class WarlockApplication extends WorkbenchAdvisor implements IApplication {
	
	public String getInitialWindowPerspectiveId ()
	{
		return WarlockPerspectiveFactory.WARLOCK_PERSPECTIVE_ID;
	}
	
	public void preWindowOpen(IWorkbenchWindowConfigurer configurer)
	{
		configurer.setShowPerspectiveBar(false);
		configurer.setShowProgressIndicator(false);
		configurer.setShowFastViewBars(false);
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
		configurer.setShowMenuBar(true);
	}
	
	public Object start(IApplicationContext context) throws Exception {
		Display display = PlatformUI.createDisplay();
		int ret = PlatformUI.createAndRunWorkbench(display, this);
		if (ret == PlatformUI.RETURN_RESTART)
			return EXIT_RESTART;
		
		return EXIT_OK;
	}
	
	public void stop() {
		
	}
}
