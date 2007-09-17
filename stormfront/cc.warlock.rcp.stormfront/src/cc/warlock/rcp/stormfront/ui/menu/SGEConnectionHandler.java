package cc.warlock.rcp.stormfront.ui.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

import cc.warlock.rcp.menu.SimpleCommandHandler;
import cc.warlock.rcp.stormfront.ui.wizards.SGEConnectWizard;
import cc.warlock.rcp.ui.WarlockWizardDialog;

public class SGEConnectionHandler extends SimpleCommandHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		WarlockWizardDialog dialog = new WarlockWizardDialog(Display.getDefault().getActiveShell(),
			new SGEConnectWizard());
		
		int response = dialog.open();
		
		return null;
	}

}
