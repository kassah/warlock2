/*
 * Created on Dec 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.rcp.application;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.arcaner.warlock.rcp.views.BarsView;
import com.arcaner.warlock.rcp.views.CompassView;
import com.arcaner.warlock.rcp.views.GameView;

/**
 * @author Marshall
 */
public class WarlockPerspectiveFactory implements IPerspectiveFactory {

	private static IPageLayout myLayout = null;
	
	public static final String WARLOCK_PERSPECTIVE_ID = "com.arcaner.warlock.warlockPerspective";
	public static final String BOTTOM_FOLDER_ID = "com.arcaner.warlock.bottomFolder";
	
	public static IPageLayout getLayout() {
		return myLayout;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	public void createInitialLayout(IPageLayout layout) {
		myLayout = layout;
		
		layout.setEditorAreaVisible(false);
		layout.addView(GameView.VIEW_ID, IPageLayout.LEFT, 0.15f, layout.getEditorArea());
//		IFolderLayout folder = layout.createFolder(BOTTOM_FOLDER_ID, IPageLayout.BOTTOM, 0.90f, GameView.VIEW_ID);
		layout.addStandaloneView(BarsView.VIEW_ID, false, IPageLayout.BOTTOM, 0.95f, GameView.VIEW_ID);
		
//		folder.addView("org.eclipse.pde.runtime.LogView");
		
		layout.addView(CompassView.VIEW_ID, IPageLayout.RIGHT, 0.85f, BarsView.VIEW_ID);
	}

}
