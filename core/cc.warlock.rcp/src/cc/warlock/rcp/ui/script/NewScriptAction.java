package cc.warlock.rcp.ui.script;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

import cc.warlock.rcp.application.WarlockApplication;

public class NewScriptAction extends Action {
	protected IWorkbenchPage page;
	protected ICommonActionExtensionSite site;
	
	public NewScriptAction (IWorkbenchPage page, ICommonActionExtensionSite site)
	{
		setText("Create a New Script");
		this.page = page;
		this.site = site;
	}
	
	@Override
	public void run() {
		
		NewScriptWizard wizard = new NewScriptWizard(page);
		WizardDialog dialog = new WizardDialog(page.getWorkbenchWindow().getShell(), wizard);
		
		int response = dialog.open();
		if (response == WizardDialog.OK)
		{
			site.getStructuredViewer().refresh(WarlockApplication.instance());
		}
	}
}
