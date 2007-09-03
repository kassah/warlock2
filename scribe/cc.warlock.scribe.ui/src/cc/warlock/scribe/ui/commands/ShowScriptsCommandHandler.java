package cc.warlock.scribe.ui.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import cc.warlock.rcp.menu.SimpleCommandHandler;
import cc.warlock.scribe.ui.views.ScriptsView;

public class ShowScriptsCommandHandler extends SimpleCommandHandler {

	protected IViewPart scriptsView = null;
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		try {
			if (scriptsView == null)
			{
				scriptsView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ScriptsView.VIEW_ID);
			} else {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(scriptsView);
				scriptsView = null;
			}
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
