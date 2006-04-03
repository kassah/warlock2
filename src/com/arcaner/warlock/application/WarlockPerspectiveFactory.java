/*
 * Created on Dec 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.arcaner.warlock.views.CompassView;
import com.arcaner.warlock.views.GameView;
import com.arcaner.warlock.views.BarsView;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WarlockPerspectiveFactory implements IPerspectiveFactory {

	private static IPageLayout myLayout = null;
	
	public static final String WARLOCK_PERSPECTIVE_ID = "com.arcaner.warlock.warlockPerspective";

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
		layout.addView(CompassView.VIEW_ID, IPageLayout.RIGHT, 0.95f, GameView.VIEW_ID);
		layout.addView(BarsView.VIEW_ID, IPageLayout.BOTTOM, 0.85f, GameView.VIEW_ID);
	}

}
