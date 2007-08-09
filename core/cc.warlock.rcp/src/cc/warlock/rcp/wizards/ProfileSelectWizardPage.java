/*
 * Created on Dec 31, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import cc.warlock.rcp.ui.WarlockSharedImages;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ProfileSelectWizardPage extends WizardPage {

	public ProfileSelectWizardPage ()
	{
		super ("Select a saved profile.", "Please select a saved profile.", WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_WIZBAN_WARLOCK));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub

	}

}
