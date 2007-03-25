/*
 * Created on Dec 30, 2004
 */
package com.arcaner.warlock.rcp.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

/**
 * @author Marshall
 */
public class WizardAction extends Action {

	private Class wizardClass;
	private boolean wasCanceled;
	
	public WizardAction (String name, Class wizardClass)
	{
		setText(name);
		setToolTipText(name);
		
		this.wizardClass = wizardClass;
		wasCanceled = false;
	}
	
	public void run() {
		try {
			WizardDialog dialog = new WizardDialog (Display.getCurrent().getActiveShell(), (IWizard) wizardClass.newInstance());
			dialog.create();
			dialog.getShell().setSize(450, 500);
			int response = dialog.open();
			
			if (response != WizardDialog.OK)
				wasCanceled = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean wasCanceled ()
	{
		return wasCanceled;
	}
}
