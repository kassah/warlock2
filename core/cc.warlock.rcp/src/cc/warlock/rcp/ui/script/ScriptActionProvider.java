package cc.warlock.rcp.ui.script;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

import cc.warlock.core.script.IScriptFileInfo;

public class ScriptActionProvider extends CommonActionProvider {

	protected Action openAction;
	
	public ScriptActionProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ICommonActionExtensionSite site) {
		ICommonViewerSite viewSite = site.getViewSite();
		
		if (viewSite instanceof ICommonViewerWorkbenchSite)
		{
			ICommonViewerWorkbenchSite wSite = (ICommonViewerWorkbenchSite) viewSite;
			
			openAction = new OpenAction(wSite.getPage(), viewSite.getSelectionProvider());
		}
	}
	
	@Override
	public void fillActionBars(IActionBars actionBars) {
		actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, openAction);
	}
	
	@Override
	public void fillContextMenu(IMenuManager menu) {
		menu.appendToGroup(ICommonMenuConstants.GROUP_OPEN, openAction);
	}
	
	protected class OpenAction extends Action 
	{
		protected IWorkbenchPage page;
		protected ISelectionProvider provider;
		
		public OpenAction (IWorkbenchPage page, ISelectionProvider provider)
		{
			setText("Edit Script");
			this.page = page;
			this.provider = provider;
		}
		
		@Override
		public void run() {
			IStructuredSelection selection = (IStructuredSelection) provider.getSelection();
			
			Object element = selection.getFirstElement();
			if (element instanceof IScriptFileInfo)
			{
				IScriptFileInfo info = (IScriptFileInfo) element;
				IFileStore fileStore=  EFS.getLocalFileSystem().getStore(new Path(info.getScriptFile().getAbsolutePath()));
				
				if (fileStore.fetchInfo().exists())
				{
					try {
						page.openEditor(new FileStoreEditorInput(fileStore), "org.eclipse.ui.DefaultTextEditor");
					} catch (PartInitException e) {
						/* some code */
					}
				}
			}
		}
		
		@Override
		public boolean isEnabled() {
			IStructuredSelection selection = (IStructuredSelection) provider.getSelection();
			
			Object element = selection.getFirstElement();
			if (element instanceof IScriptFileInfo)
			{
				return true;
			}
			return false;
		}
	}
	
}
