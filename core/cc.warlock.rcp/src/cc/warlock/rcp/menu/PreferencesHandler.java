package cc.warlock.rcp.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;

import cc.warlock.client.IWarlockClient;
import cc.warlock.client.IWarlockClientListener;
import cc.warlock.client.WarlockClientRegistry;
import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.prefs.CharacterPreferencesPage;
import cc.warlock.rcp.prefs.HighlightNamesPreferencePage;
import cc.warlock.rcp.prefs.HighlightStringsPreferencePage;
import cc.warlock.rcp.prefs.LookAndFeelPreferencePage;
import cc.warlock.rcp.prefs.PresetsPreferencePage;
import cc.warlock.rcp.views.GameView;


public class PreferencesHandler extends SimpleCommandHandler
{
	protected boolean enabled = false;
	protected boolean added = false;
	
	@Override
	public boolean isEnabled() {
		if (!added)
		{
			final IStormFrontClient currentClient = Warlock2Plugin.getDefault().getCurrentClient();
			
			if (currentClient.getConnection() != null)
			{
				enabled = true;
				added = true;
			}
			else {
				WarlockClientRegistry.addWarlockClientListener(new IWarlockClientListener() {
					public void clientActivated(IWarlockClient client) {}
					public void clientConnected(IWarlockClient client) {
						if (client == currentClient) {
							enabled = true;
							WarlockClientRegistry.removeWarlockClientListener(this);
						}
					}
					public void clientDisconnected(IWarlockClient client) {}
					public void clientRemoved(IWarlockClient client) {}
				});
				added = true;
			}
		}
		
		return enabled;
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
