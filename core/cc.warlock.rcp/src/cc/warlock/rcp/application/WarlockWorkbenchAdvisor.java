package cc.warlock.rcp.application;

import java.util.Timer;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import cc.warlock.core.configuration.WarlockConfiguration;

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

	public IWorkbenchWindowConfigurer getWindowConfigurer(IWorkbenchWindow w) {
		return getWorkbenchConfigurer().getWindowConfigurer(w);
	}
}
