package cc.warlock.rcp.stormfront.ui.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

import cc.warlock.rcp.menu.SimpleCommandHandler;
import cc.warlock.rcp.stormfront.ui.wizards.ImportServerSettingsWizard;

public class ImportStormfrontSettingsHandler extends SimpleCommandHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ImportServerSettingsWizard wizard = new ImportServerSettingsWizard();
		WizardDialog dialog = new WizardDialog(
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
			wizard);
		
		int response = dialog.open();
		if (response == Dialog.OK) {
			
		}

		return null;
	}

}
