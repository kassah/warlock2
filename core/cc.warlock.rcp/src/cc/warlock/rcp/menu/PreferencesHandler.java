package cc.warlock.rcp.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;

import cc.warlock.rcp.prefs.LookAndFeelPreferencePage;
import cc.warlock.rcp.views.GameView;


public class PreferencesHandler extends SimpleCommandHandler
{	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		PreferenceDialog dialog = PreferencesUtil.createPropertyDialogOn(Display.getDefault().getActiveShell(),
				GameView.getViewInFocus(), LookAndFeelPreferencePage.PAGE_ID, null, null);
		
		int response = dialog.open();
		
		return null;
	}

}
