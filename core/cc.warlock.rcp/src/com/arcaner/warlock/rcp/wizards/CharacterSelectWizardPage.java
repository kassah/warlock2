/*
 * Created on Dec 31, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.rcp.wizards;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.arcaner.warlock.configuration.Account;
import com.arcaner.warlock.configuration.Profile;
import com.arcaner.warlock.configuration.SavedProfiles;
import com.arcaner.warlock.network.SGEConnection;
import com.arcaner.warlock.network.SGEConnectionListener;
import com.arcaner.warlock.rcp.ui.WarlockSharedImages;
import com.arcaner.warlock.rcp.ui.network.SWTSGEConnectionListenerAdapter;
import com.arcaner.warlock.rcp.util.LoginUtil;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CharacterSelectWizardPage extends WizardPage {

	private Table characters;
	private Map<String,String> characterMap;
	private TableViewer characterViewer;
	private String selectedCharacterCode;
	private Listener listener;
	private boolean complete;
	
	public CharacterSelectWizardPage (SGEConnection connection)
	{
		super ("Select a character.", "Please select a character,", WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_WIZBAN_WARLOCK));
		complete = false;
		
		this.characterMap = new HashMap<String,String>();
		
		listener = new Listener();
		connection.addSGEConnectionListener(new SWTSGEConnectionListenerAdapter(listener));
		System.out.println("CharacterSelectWizardPage constructor");
	}
	
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite controls = new Composite(parent, SWT.NONE);
		FillLayout layout = new FillLayout();
		controls.setLayout(layout);
		
		characters = new Table(controls, SWT.BORDER | SWT.SINGLE);
		characters.setEnabled(false);
		
		characterViewer = new TableViewer (characters);
		characterViewer.setContentProvider(new CharacterContentProvider());
		characterViewer.setLabelProvider(new CharacterLabelProvider());
		characterViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				characterSelected(event.getSelection());
				complete = true;
				getWizard().getContainer().updateButtons();
			}
		});
		
		characterViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				characterSelected(characterViewer.getSelection());
				
				complete = true;
				getWizard().performFinish();
				getWizard().dispose();
			}
		});
		setControl(controls);
	}

	private class CharacterContentProvider implements IStructuredContentProvider {
		
		public Object[] getElements(Object inputElement) {
			Map<String,String> characterMap = (Map<String,String>) inputElement;
			return characterMap.keySet().toArray();
		}
		
		public void dispose() {
			// TODO Auto-generated method stub

		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}
	}
	
	private class CharacterLabelProvider extends LabelProvider {

		public String getText(Object element) {
			return (String) characterMap.get(element);
		}
		
		public Image getImage(Object element) {
			return WarlockSharedImages.getImage(WarlockSharedImages.IMG_CHARACTER);
		}
	}
	
	private void characterSelected (ISelection selection)
	{
		if (selection instanceof IStructuredSelection)
		{
			IStructuredSelection selection2 = (IStructuredSelection) selection;
			selectedCharacterCode = (String) selection2.getFirstElement();
		}
	}
	
	/**
	 * @return Returns the selectedCharacterCode.
	 */
	public String getSelectedCharacterCode() {
		return selectedCharacterCode;
	}
	
	public String getSelectedCharacterName() {
		return (String) characterMap.get(selectedCharacterCode);
	}
	
	public Listener getListener ()
	{
		return listener;
	}
	
	
	public boolean isPageComplete() {
		return complete;
	}
	
	public class Listener extends SGEConnectionListener {
		private IProgressMonitor monitor;
		
		public void setProgressMonitor (IProgressMonitor monitor)
		{
			this.monitor = monitor;
		}
		
		public void charactersReady(SGEConnection connection, Map<String,String> characters) {
//			CharacterSelectWizardPage.this.characterMap.clear();
			CharacterSelectWizardPage.this.characterMap = characters;
			characterViewer.setInput(characters);
			CharacterSelectWizardPage.this.characters.setEnabled(true);
		}
		
		public void readyToPlay (SGEConnection connection, Map<String,String> loginProperties)
		{
			if (monitor != null) {
				monitor.worked(1);
				//monitor.done();
			}
			
			GameSelectWizardPage gameSelectPage = (GameSelectWizardPage) getPreviousPage();
			AccountWizardPage accountPage = (AccountWizardPage) getPreviousPage().getPreviousPage();
			
			Account account = accountPage.getSavedAccount();
			if (account != null)
			{
				Collection<Profile> savedProfiles = SavedProfiles.getProfiles(account);

				boolean exists = false;
				if (savedProfiles != null && savedProfiles.size() > 0)
				{
					for (Profile profile : savedProfiles)
					{
						if (getSelectedCharacterName().equals(profile.getCharacterName())) {
							exists = true; break;
						}
					}
				}
					
				if (!exists) {
					boolean response = MessageDialog.openQuestion(getShell(),
						"Save Profile?",
						"Would you like to save the character \"" + getSelectedCharacterName() + "\" as a new profile?");
					
					if (response)
					{
						SavedProfiles.addProfile(account, getSelectedCharacterCode(), getSelectedCharacterName(),
							gameSelectPage.getSelectedGameCode(), gameSelectPage.getSelectedGameName());
					}
				}
			}
			
			LoginUtil.connectAndOpenGameView(loginProperties);
			
			
		}
	}
	
}
