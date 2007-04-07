package com.arcaner.warlock.rcp.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;

import com.arcaner.warlock.rcp.plugin.Warlock2Plugin;
import com.arcaner.warlock.rcp.prefs.LookAndFeelPreferencePage;
import com.arcaner.warlock.rcp.prefs.StandardColorsAndFontsPreferencePage;

public class PreferencesHandler extends SimpleCommandHandler
{
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(Display.getDefault().getActiveShell(),
				StandardColorsAndFontsPreferencePage.PAGE_ID,
			new String[] {
				LookAndFeelPreferencePage.PAGE_ID,
				StandardColorsAndFontsPreferencePage.PAGE_ID
			}, null);
		
		// remove all non-warlock preference nodes
		IPreferenceNode[] nodes = dialog.getPreferenceManager().getRootSubNodes();
		for (IPreferenceNode node : nodes)
		{
			if (!node.getId().startsWith(Warlock2Plugin.PLUGIN_ID))
			{
				dialog.getPreferenceManager().remove(node);
			}
		}
		
		int response = dialog.open();
		
		return null;
	}

}
