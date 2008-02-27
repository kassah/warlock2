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
/**
 * 
 */
package cc.warlock.rcp.ui.script;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
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