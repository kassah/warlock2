package cc.warlock.rcp.application;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import cc.warlock.rcp.menu.ScriptsWindowHandler;

public class WarlockWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	public WarlockWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
		configurer.setShowPerspectiveBar(false);
		configurer.setShowProgressIndicator(false);
		configurer.setShowFastViewBars(false);
		configurer.setShowStatusLine(false);
		configurer.setShowMenuBar(true);
		configurer.setShowCoolBar(false);
	}
	
	@Override
	public boolean preWindowShellClose() {
		IWorkbenchWindow window = getWindowConfigurer().getWindow();
		if (ScriptsWindowHandler.blockClose(window)) {
			ScriptsWindowHandler.activate();
			return false;
		} else {
			return true;
		}
	}
}
