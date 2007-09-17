package cc.warlock.core.stormfront;


import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class StormFrontCorePlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "cc.warlock.core.stormfront";

	// The shared instance
	private static StormFrontCorePlugin plugin;
	
	/**
	 * The constructor
	 */
	public StormFrontCorePlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
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
	public static StormFrontCorePlugin getDefault() {
		return plugin;
	}

}
