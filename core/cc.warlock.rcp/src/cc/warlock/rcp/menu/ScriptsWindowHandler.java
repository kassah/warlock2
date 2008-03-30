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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.rcp.application.ScriptsPerspectiveFactory;
import cc.warlock.rcp.application.WarlockApplication;
import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.views.GameView;

public class ScriptsWindowHandler extends SimpleCommandHandler implements
		IHandler {
	
	protected static IWorkbenchWindow window;
	
	public static void activate() {
		window.getShell().forceActive();
	}
	
	public static boolean blockClose(IWorkbenchWindow other) {
		return window != null && !window.equals(other);
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (window != null) {
			activate();
			return null;
		}
		
		try {
			window = PlatformUI.getWorkbench().openWorkbenchWindow(ScriptsPerspectiveFactory.PERSPECTIVE_ID, WarlockApplication.instance());
		} catch (WorkbenchException e) {
			throw new ExecutionException("Unable to create script editor window", e);
		}
		IWorkbenchWindowConfigurer c = WarlockApplication.instance().getWindowConfigurer(window);
		c.setShowMenuBar(false);
		c.setTitle("Warlock Scripts");
		c.setInitialSize(new Point(600, 400));
		c.setShowCoolBar(true);
		
		// Add Dispose Listener, so that if the window closes, we can clear our static variable
		DisposeListener listener = new DisposeListener() {
		    public void widgetDisposed(DisposeEvent event) {
		        window = null;
		    }
		};
		
		window.getShell().addDisposeListener(listener);
		
		return null;
	}

}
