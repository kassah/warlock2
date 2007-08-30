package cc.warlock.rcp.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

import cc.warlock.rcp.ui.WarlockWizardDialog;
import cc.warlock.rcp.wizards.ConnectWizard;

public class NewConnectionHandler extends SimpleCommandHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		WarlockWizardDialog dialog = new WarlockWizardDialog(Display.getDefault().getActiveShell(),
			new ConnectWizard());
		
		int response = dialog.open();
		
		return null;
	}

}
