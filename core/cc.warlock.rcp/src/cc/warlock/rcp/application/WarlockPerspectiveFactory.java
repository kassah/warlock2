/*
 * Created on Dec 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;

import cc.warlock.rcp.views.BarsView;
import cc.warlock.rcp.views.GameView;
import cc.warlock.rcp.views.HandsView;
import cc.warlock.rcp.views.StreamView;

/**
 * @author Marshall
 */
public class WarlockPerspectiveFactory implements IPerspectiveFactory {

	private static IPageLayout myLayout = null;
	
	public static final String WARLOCK_PERSPECTIVE_ID = "com.arcaner.warlock.warlockPerspective";
	public static final String BOTTOM_FOLDER_ID = "com.arcaner.warlock.bottomFolder";
	public static final String TOP_FOLDER_ID = "com.arcaner.warlock.topFolder";
	public static final String RIGHT_FOLDER_ID = "com.arcaner.warlock.rightFolder";
	public static final String LEFT_FOLDER_ID = "com.arcaner.warlock.leftFolder";
	
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
		layout.addStandaloneView(HandsView.VIEW_ID, false, IPageLayout.TOP, 0.05f, GameView.VIEW_ID);
		
		IPlaceholderFolderLayout topFolder =
			layout.createPlaceholderFolder(TOP_FOLDER_ID, IPageLayout.TOP, 0.15f, HandsView.VIEW_ID);
		
		topFolder.addPlaceholder(StreamView.DEATH_VIEW_ID);
		topFolder.addPlaceholder(StreamView.THOUGHTS_VIEW_ID);
		
		IPlaceholderFolderLayout rightFolder =
			layout.createPlaceholderFolder(RIGHT_FOLDER_ID, IPageLayout.RIGHT, 0.80f, GameView.VIEW_ID);
		
		rightFolder.addPlaceholder(StreamView.INVENTORY_VIEW_ID);
		
//		IFolderLayout folder = layout.createFolder(BOTTOM_FOLDER_ID, IPageLayout.BOTTOM, 0.90f, GameView.VIEW_ID);
		layout.addStandaloneView(BarsView.VIEW_ID, false, IPageLayout.BOTTOM, 0.95f, GameView.VIEW_ID);
		
//		folder.addView("org.eclipse.pde.runtime.LogView");
		
//		layout.addView(CompassView.VIEW_ID, IPageLayout.RIGHT, 0.85f, BarsView.VIEW_ID);
	}

}
