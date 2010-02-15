package cc.warlock.rcp.application;

import java.util.Timer;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class WarlockWorkbenchAdvisor extends WorkbenchAdvisor {
	private Timer timer = new Timer();

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new WarlockWorkbenchWindowAdvisor(configurer);
	}

	@Override	
	public String getInitialWindowPerspectiveId ()
	{
		return WarlockPerspectiveFactory.WARLOCK_PERSPECTIVE_ID;
	}
	
	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		configurer.setSaveAndRestore(true);
		if (WarlockUpdates.autoUpdate())
		{
			Thread updateTask = new Thread() {
				public void run ()
				{
					WarlockUpdates.checkForUpdates(new NullProgressMonitor());
				}
			};
			updateTask.start();
		}
	}
	
	@Override
	public IAdaptable getDefaultPageInput() {
		return null;
	}
	
	@Override
	public boolean preShutdown() {
		timer.cancel();
		
		return true;
	}

	public IWorkbenchWindowConfigurer getWindowConfigurer(IWorkbenchWindow w) {
		return getWorkbenchConfigurer().getWindowConfigurer(w);
	}
}
