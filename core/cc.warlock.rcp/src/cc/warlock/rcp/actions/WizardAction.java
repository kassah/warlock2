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
/*
 * Created on Dec 30, 2004
 */
package cc.warlock.rcp.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import cc.warlock.rcp.ui.WarlockWizardDialog;

/**
 * @author Marshall
 */
public class WizardAction extends Action {

	private Class<? extends IWizard> wizardClass;
	private boolean wasCanceled;
	
	public WizardAction (String name, Class<? extends IWizard> wizardClass, ImageDescriptor descriptor)
	{
		setText(name);
		setToolTipText(name);
		setImageDescriptor(descriptor);
		
		this.wizardClass = wizardClass;
		wasCanceled = false;
	}
	
	public void run() {
		try {
			WarlockWizardDialog dialog = new WarlockWizardDialog (Display.getCurrent().getActiveShell(), wizardClass.newInstance());
			dialog.create();
			dialog.getShell().setSize(450, 500);
			int response = dialog.open();
			
			if (response != WizardDialog.OK)
				wasCanceled = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean wasCanceled ()
	{
		return wasCanceled;
	}
}
