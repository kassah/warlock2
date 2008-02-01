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
package cc.warlock.rcp.ui.script;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

import cc.warlock.rcp.application.WarlockApplication;

public class NewScriptAction extends Action {
	protected IWorkbenchPage page;
	protected ICommonActionExtensionSite site;
	
	public NewScriptAction (IWorkbenchPage page, ICommonActionExtensionSite site)
	{
		setText("Create a New Script");
		this.page = page;
		this.site = site;
	}
	
	@Override
	public void run() {
		
		NewScriptWizard wizard = new NewScriptWizard(page);
		WizardDialog dialog = new WizardDialog(page.getWorkbenchWindow().getShell(), wizard);
		
		int response = dialog.open();
		if (response == WizardDialog.OK)
		{
			site.getStructuredViewer().refresh(WarlockApplication.instance());
		}
	}
}
