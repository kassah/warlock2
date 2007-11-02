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
