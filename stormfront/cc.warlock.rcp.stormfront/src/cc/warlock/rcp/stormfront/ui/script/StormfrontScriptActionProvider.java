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
package cc.warlock.rcp.stormfront.ui.script;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

import cc.warlock.core.stormfront.serversettings.server.IServerScriptInfo;

public class StormfrontScriptActionProvider extends CommonActionProvider {

	protected Action openAction;
	
	public StormfrontScriptActionProvider() {
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
			if (element instanceof IServerScriptInfo)
			{
				IServerScriptInfo info = (IServerScriptInfo) element;
				
				try {
					page.openEditor(new ServerScriptEditorInput(info), "org.eclipse.ui.DefaultTextEditor");
				} catch (PartInitException e) {
					/* some code */
				}
			}
		}
		
		@Override
		public boolean isEnabled() {
			IStructuredSelection selection = (IStructuredSelection) provider.getSelection();
			
			Object element = selection.getFirstElement();
			if (element instanceof IServerScriptInfo)
			{
				return true;
			}
			return false;
		}
	}

}
