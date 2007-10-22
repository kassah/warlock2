package cc.warlock.rcp.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.ui.PlatformUI;

import cc.warlock.rcp.application.WarlockUpdates;

public class CheckForUpdatesHandler extends SimpleCommandHandler implements
		IHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		dialog.setBlockOnOpen(false);
		dialog.open();
		
		WarlockUpdates.checkForUpdates(dialog.getProgressMonitor());
		
		dialog.close();
		
		return null;
	}

}
