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
package cc.warlock.rcp.stormfront.ui;

import org.eclipse.ui.IPageLayout;

import cc.warlock.rcp.application.WarlockPerspectiveFactory;
import cc.warlock.rcp.stormfront.ui.views.BarsView;
import cc.warlock.rcp.stormfront.ui.views.HandsView;

public class StormFrontPerspectiveFactory extends WarlockPerspectiveFactory {

	public static final String PERSPECTIVE_ID = "cc.warlock.stormfrontPerspective";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		super.createInitialLayout(layout);
		
		layout.addStandaloneViewPlaceholder(HandsView.VIEW_ID, IPageLayout.TOP, 0.05f, MAIN_FOLDER_ID, false);
		layout.addStandaloneViewPlaceholder(BarsView.VIEW_ID, IPageLayout.BOTTOM, 0.95f, MAIN_FOLDER_ID, false);
		
//		layout.addPlaceholder(StatusView.VIEW_ID, IPageLayout.RIGHT, 0.8f, HandsView.VIEW_ID);
	}
}
