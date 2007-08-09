/*
 * Created on Dec 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import cc.warlock.network.SGEConnection;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConnectWizard extends WizardWithNotification {

	private SGEConnection sgeConnection;
	private AccountWizardPage page1;
	private GameSelectWizardPage page2;
	private CharacterSelectWizardPage page3;

	public ConnectWizard ()
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
