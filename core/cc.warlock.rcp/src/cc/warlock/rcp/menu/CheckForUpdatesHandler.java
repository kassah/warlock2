package cc.warlock.rcp.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.NullProgressMonitor;

import cc.warlock.rcp.application.WarlockUpdates;

public class CheckForUpdatesHandler extends SimpleCommandHandler implements
		IHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		WarlockUpdates.checkForUpdates(new NullProgressMonitor());
		
		
		return null;
	}

}
