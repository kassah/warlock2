package cc.warlock.rcp.stormfront.ui.wizards;

import org.eclipse.osgi.util.NLS;

public class WizardMessages extends NLS {
	private static final String BUNDLE_NAME = "cc.warlock.rcp.stormfront.ui.wizards.messages"; //$NON-NLS-1$
	public static String AccountWizardPage_title;
	public static String AccountWizardPage_description;
	public static String AccountWizardPage_label_accountName;
	public static String AccountWizardPage_label_password;
	public static String AccountWizardPage_saveAccount_title;
	public static String AccountWizardPage_saveAccount_description;
	public static String AccountWizardPage_progressMessage;
	public static String AccountWizardPage_loginError_accountRejected;
	public static String AccountWizardPage_loginError_accountInvalid;
	public static String AccountWizardPage_loginError_passwordInvalid;
	public static String AccountWizardPage_loginError_title;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, WizardMessages.class);
	}
}
