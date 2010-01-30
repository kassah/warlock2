/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.rcp.menu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.rcp.ui.client.WarlockClientAdaptable;
import cc.warlock.rcp.views.GameView;


public class PreferencesHandler extends SimpleCommandHandler
{	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		//IWarlockClient activeClient;
		GameView inFocus = GameView.getGameViewInFocus();
		//if (inFocus != null)
		//{
		IWarlockClient activeClient = inFocus.getWarlockClient();
		//}
		
		PreferenceDialog dialog = PreferencesUtil.createPropertyDialogOn(Display.getDefault().getActiveShell(),
				new WarlockClientAdaptable(activeClient), null, null, null);
		
		dialog.getTreeViewer().expandToLevel(2);
		
		int response = dialog.open();
		
		return null;
	}

}
