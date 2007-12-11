/**
 * 
 */
package cc.warlock.rcp.ui.script;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;

import cc.warlock.core.script.IScriptFileInfo;

public class OpenScriptAction extends Action 
{
	protected IWorkbenchPage page;
	protected ISelectionProvider provider;
	
	public OpenScriptAction (IWorkbenchPage page, ISelectionProvider provider)
	{
		setText("Open with Text Editor");
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
			IFileStore fileStore = EFS.getLocalFileSystem().fromLocalFile(info.getScriptFile());
			
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