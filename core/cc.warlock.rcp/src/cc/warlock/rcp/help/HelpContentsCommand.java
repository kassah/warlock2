package cc.warlock.rcp.help;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cc.warlock.rcp.menu.SimpleCommandHandler;

public class HelpContentsCommand extends SimpleCommandHandler {

	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchAction action = ActionFactory.HELP_CONTENTS.create(
			PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		action.run();
		
		return null;
	}
}
