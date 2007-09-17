package cc.warlock.rcp.ui;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class WarlockWizardDialog extends WizardDialog {

	public WarlockWizardDialog (Shell shell, IWizard wizard)
	{
		super(shell, wizard);
	}
	
	public void finish ()
	{
		finishPressed();
	}
}
