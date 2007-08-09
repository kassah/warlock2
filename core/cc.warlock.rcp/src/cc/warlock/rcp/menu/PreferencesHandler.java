package cc.warlock.rcp.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;

import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.rcp.prefs.CharacterPreferencesPage;
import cc.warlock.rcp.prefs.HighlightNamesPreferencePage;
import cc.warlock.rcp.prefs.HighlightStringsPreferencePage;
import cc.warlock.rcp.prefs.LookAndFeelPreferencePage;
import cc.warlock.rcp.prefs.PresetsPreferencePage;
import cc.warlock.rcp.views.GameView;


public class PreferencesHandler extends SimpleCommandHandler
{
	
	@Override
	public boolean isEnabled() {
		GameView inFocus = GameView.getViewInFocus();
		if (inFocus != null)
		{
			IStormFrontClient client = inFocus.getStormFrontClient();
			if (client != null)
			{
				return client.getCharacterName() != null;
			}
		}
		return false;
	}
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		PreferenceDialog dialog = PreferencesUtil.createPropertyDialogOn(Display.getDefault().getActiveShell(),
				GameView.getViewInFocus(), 
				HighlightStringsPreferencePage.PAGE_ID,
				new String[] {
					CharacterPreferencesPage.PAGE_ID,
					LookAndFeelPreferencePage.PAGE_ID,
					PresetsPreferencePage.PAGE_ID,
					HighlightStringsPreferencePage.PAGE_ID,
					HighlightNamesPreferencePage.PAGE_ID
				}, null);
//		PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(Display.getDefault().getActiveShell(),
//				PresetsPreferencePage.PAGE_ID,
//			new String[] {
//				LookAndFeelPreferencePage.PAGE_ID,
//				PresetsPreferencePage.PAGE_ID
//			}, null);
		
		// remove all non-warlock preference nodes
//		IPreferenceNode[] nodes = dialog.getPreferenceManager().getRootSubNodes();
//		for (IPreferenceNode node : nodes)
//		{
//			if (!node.getId().startsWith(Warlock2Plugin.PLUGIN_ID))
//			{
//				dialog.getPreferenceManager().remove(node);
//			}
//		}
		
		int response = dialog.open();
		
		return null;
	}

}
