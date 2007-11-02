package cc.warlock.rcp.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.rcp.application.ScriptsPerspectiveFactory;
import cc.warlock.rcp.application.WarlockApplication;
import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.ui.client.WarlockClientAdaptable;
import cc.warlock.rcp.views.GameView;

public class ScriptsWindowHandler extends SimpleCommandHandler implements
		IHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWarlockClient activeClient = Warlock2Plugin.getDefault().getCurrentClient();
		GameView inFocus = GameView.getViewInFocus();
		if (inFocus != null)
		{
			activeClient = inFocus.getWarlockClient();
		}
		
		try {
			WarlockApplication.instance().setShowMenus(false);
			WarlockApplication.instance().setWindowTitle("Warlock Scripts");
			WarlockApplication.instance().setInitialSize(new Point(800, 600));
			IWorkbenchWindow window = 
				PlatformUI.getWorkbench().openWorkbenchWindow(ScriptsPerspectiveFactory.PERSPECTIVE_ID, WarlockApplication.instance());
			
			WarlockApplication.instance().setShowMenus(true);
			
		} catch (WorkbenchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
