package com.arcaner.warlock.rcp.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;

/**
 * @author rob.stryker@jboss.com
 */
public abstract class WizardPageWithNotification extends WizardPage implements IWizardPage {
	/**
	 * @param pageName
	 */
	protected WizardPageWithNotification(String pageName) {
		super(pageName);
	}
	
    protected WizardPageWithNotification(String pageName, String title,
            ImageDescriptor titleImage) {
    	super(pageName, title, titleImage);
    }

	
    public void pageEntered(int button) {}
    public void pageExited(int button) {}
}
