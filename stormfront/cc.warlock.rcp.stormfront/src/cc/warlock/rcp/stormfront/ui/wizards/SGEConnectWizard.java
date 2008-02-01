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
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.stormfront.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.rcp.wizards.WizardWithNotification;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SGEConnectWizard extends WizardWithNotification {

	private SGEConnection sgeConnection;
	private AccountWizardPage page1;
	private GameSelectWizardPage page2;
	private CharacterSelectWizardPage page3;

	public SGEConnectWizard ()
	{
		setNeedsProgressMonitor(true);
	}
	
	public boolean performFinish() {
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException
				{
					page3.getListener().setProgressMonitor(monitor);
					
					monitor.beginTask("Logging in \"" + page3.getSelectedCharacterName() + "\"...", 2);
					sgeConnection.selectCharacter(page3.getSelectedCharacterCode());
					monitor.worked(1);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return true;
	}

	public void addPages() {
		// TODO Auto-generated method stub
		
		sgeConnection = new SGEConnection();
		page1 = new AccountWizardPage(sgeConnection);
		page2 = new GameSelectWizardPage(sgeConnection);
		page3 = new CharacterSelectWizardPage(sgeConnection);
		
		addPage(page1);
		addPage(page2);
		addPage(page3);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	public boolean canFinish() {
		return page3.isPageComplete();
	}
}
