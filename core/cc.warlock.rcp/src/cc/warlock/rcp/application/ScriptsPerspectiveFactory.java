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
package cc.warlock.rcp.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class ScriptsPerspectiveFactory implements IPerspectiveFactory {

	public static final String PERSPECTIVE_ID = "cc.warlock.scriptsPerspective";
	
	public static final String SCRIPT_NAVIGATOR_VIEW_ID = "cc.warlock.rcp.views.scriptNavigatorView";
	
	public void createInitialLayout(IPageLayout layout) {
		
		layout.addView(SCRIPT_NAVIGATOR_VIEW_ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		layout.setEditorAreaVisible(true);
		
	}

}
