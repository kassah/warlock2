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
	private boolean debugMode = false;
	private Timer timer = new Timer();
	private boolean showMenus = true;
	private String windowTitle = null;
	private Point initialSize = null;
	
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
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
		configurer.setShowMenuBar(showMenus);
		if (windowTitle != null)
			configurer.setTitle(windowTitle);
		if (initialSize != null)
			configurer.setInitialSize(initialSize);
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
		
		Switch debugSwitch = new Switch("debugConsole", 'd', "debugConsole", "Start Warlock in debug mode (allows raw input/output mode)");
		debugSwitch.setDefault("false");
		
		try {
			jsap.registerParameter(profileOption);
			jsap.registerParameter(debugSwitch);
		} catch (JSAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSAPResult result = jsap.parse(arguments);
		if (result.contains("profile"))
		{
			startWithProfile = result.getString("profile");
		}
		if (result.contains("debugConsole"))
		{
			if (result.getBoolean("debugConsole"))
			{
				debugMode = true;
				
			}
		}
	}
	
	@Override
	public void preStartup() {
		WarlockConfiguration.getMainConfiguration().addConfigurationProvider(WarlockPerspectiveLayout.instance());
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
	
	public boolean inDebugMode() {
		return debugMode;
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
}
