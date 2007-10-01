package cc.warlock.rcp.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.ui.client.WarlockClientAdaptable;
import cc.warlock.rcp.views.GameView;


public class PreferencesHandler extends SimpleCommandHandler
{	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWarlockClient activeClient = Warlock2Plugin.getDefault().getCurrentClient();
		GameView inFocus = GameView.getViewInFocus();
		if (inFocus != null)
		{
			activeClient = inFocus.getWarlockClient();
		}
		
		PreferenceDialog dialog = PreferencesUtil.createPropertyDialogOn(Display.getDefault().getActiveShell(),
				new WarlockClientAdaptable(activeClient), null, null, null);
		
		dialog.getTreeViewer().expandToLevel(2);
		
		int response = dialog.open();
		
		return null;
	}

}
