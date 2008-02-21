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
		GameView inFocus = GameView.getGameViewInFocus();
		if (inFocus != null)
		{
			activeClient = inFocus.getWarlockClient();
		}
		
		try {
			WarlockApplication.instance().setShowMenus(false);
			WarlockApplication.instance().setWindowTitle("Warlock Scripts");
			WarlockApplication.instance().setInitialSize(new Point(800, 600));
			WarlockApplication.instance().setShowCoolBar(true);
			
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
