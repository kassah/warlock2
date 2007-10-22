package cc.warlock.rcp.stormfront.ui;

import org.eclipse.ui.IPageLayout;

import cc.warlock.rcp.application.WarlockPerspectiveFactory;
import cc.warlock.rcp.stormfront.ui.views.BarsView;
import cc.warlock.rcp.stormfront.ui.views.HandsView;
import cc.warlock.rcp.stormfront.ui.views.StatusView;

public class StormFrontPerspectiveFactory extends WarlockPerspectiveFactory {

	public static final String PERSPECTIVE_ID = "cc.warlock.stormfrontPerspective";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		super.createInitialLayout(layout);
		
		layout.addStandaloneViewPlaceholder(HandsView.VIEW_ID, IPageLayout.TOP, 0.05f, MAIN_FOLDER_ID, false);
		layout.addStandaloneViewPlaceholder(BarsView.VIEW_ID, IPageLayout.BOTTOM, 0.95f, MAIN_FOLDER_ID, false);
		
		layout.addStandaloneView(StatusView.VIEW_ID, false, IPageLayout.RIGHT, .5f, HandsView.VIEW_ID);
	}
}
