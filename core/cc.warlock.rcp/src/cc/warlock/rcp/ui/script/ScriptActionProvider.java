package cc.warlock.rcp.ui.script;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;


public class ScriptActionProvider extends CommonActionProvider {

	protected Action openAction;
	protected Action newScriptAction;
	
	public ScriptActionProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ICommonActionExtensionSite site) {
		ICommonViewerSite viewSite = site.getViewSite();
		
		if (viewSite instanceof ICommonViewerWorkbenchSite)
		{
			ICommonViewerWorkbenchSite wSite = (ICommonViewerWorkbenchSite) viewSite;
			
			openAction = new OpenScriptAction(wSite.getPage(), viewSite.getSelectionProvider());
			newScriptAction = new NewScriptAction(wSite.getPage(), site);
		}
	}
	
	@Override
	public void fillActionBars(IActionBars actionBars) {
		actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, openAction);
	}
	
	@Override
	public void fillContextMenu(IMenuManager menu) {
		menu.appendToGroup(ICommonMenuConstants.GROUP_OPEN, openAction);
		menu.appendToGroup(ICommonMenuConstants.GROUP_NEW, newScriptAction);
	}
	
}
