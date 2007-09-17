package cc.warlock.rcp.views;

import org.eclipse.osgi.util.NLS;

public class ViewMessages extends NLS {
	private static final String BUNDLE_NAME = "cc.warlock.rcp.views.messages"; //$NON-NLS-1$
	public static String ConnectionView_closeWindowButton;
	public static String ConnectionView_formTitle;
	public static String ConnectionView_welcomeMessage;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, ViewMessages.class);
	}

	private ViewMessages() {
	}
}
