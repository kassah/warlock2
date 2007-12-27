package cc.warlock.rcp.stormfront.ui.prefs;

import java.util.Collection;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import cc.warlock.core.configuration.Account;
import cc.warlock.core.configuration.Profile;
import cc.warlock.core.stormfront.ProfileConfiguration;
import cc.warlock.rcp.ui.WarlockSharedImages;

public class AccountsPreferencePage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.stormfront.ui.prefs.accountsAndProfiles";
	
	protected TreeViewer accounts, profiles;
	protected Button addAccount, removeAccount, editAccount, addProfile, removeProfile;
	
	@Override
	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Group accountGroup = new Group(main, SWT.NONE);
		accountGroup.setText("Accounts");
		accountGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		accountGroup.setLayout(new GridLayout(2, false));
		
		accounts = createTreeViewer(accountGroup, new ILabelProvider() {
			public void addListener(ILabelProviderListener listener) {}
			public void dispose() {}
			public Image getImage(Object element) {
				return null;
			}
			public String getText(Object element) {
				return ((Account)element).getAccountName();
			}
			public boolean isLabelProperty(Object element, String property) { return true; }
			public void removeListener(ILabelProviderListener listener) {}
		});
		
		Composite accountButtons = new Composite(accountGroup, SWT.NONE);
		accountButtons.setLayout(new FillLayout(SWT.VERTICAL));
		accountButtons.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
		
		addAccount = new Button(accountButtons, SWT.PUSH);
		addAccount.setText("Add Account");
		addAccount.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_ADD));
		addAccount.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				addAccountClicked();
			}
		});
		
		removeAccount = new Button(accountButtons, SWT.PUSH);
		removeAccount.setText("Remove Account");
		removeAccount.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_REMOVE));
		removeAccount.setEnabled(false);
		removeAccount.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				removeAccountClicked();
			}
		});
		
		editAccount = new Button(accountButtons, SWT.PUSH);
		editAccount.setText("Edit Account");
		editAccount.setEnabled(false);
		editAccount.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				editAccountClicked();
			}
		});
		
		Group profileGroup = new Group(main, SWT.NONE);
		profileGroup.setText("Profiles");
		profileGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		profileGroup.setLayout(new GridLayout(2, false));
		
		profiles = createTreeViewer(profileGroup, new ILabelProvider() {
			public void addListener(ILabelProviderListener listener) {}
			public void dispose() {}
			public Image getImage(Object element) {
				return WarlockSharedImages.getImage(WarlockSharedImages.IMG_CHARACTER);
			}
			public String getText(Object element) {
				return ((Profile)element).getName();
			}
			public boolean isLabelProperty(Object element, String property) { return true; }
			public void removeListener(ILabelProviderListener listener) {}
		});
		
		Composite profileButtons = new Composite(profileGroup, SWT.NONE);
		profileButtons.setLayout(new FillLayout(SWT.VERTICAL));
		profileButtons.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false));
		
		addProfile = new Button(profileButtons, SWT.PUSH);
		addProfile.setText("Add Profile");
		addProfile.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_ADD));
		addProfile.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				addProfileClicked();
			}
		});
		
		removeProfile = new Button(profileButtons, SWT.PUSH);
		removeProfile.setText("Remove Profile");
		removeProfile.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_REMOVE));
		removeProfile.setEnabled(false);
		removeProfile.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				removeProfileClicked();
			}
		});
	
		updateData();
		return main;
	}
	
	protected TreeViewer createTreeViewer (Composite parent, ILabelProvider provider)
	{
		final TreeViewer viewer = new TreeViewer(parent, SWT.BORDER | SWT.SINGLE | SWT.H_SCROLL);
		viewer.getTree().setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, true, true));
		viewer.setContentProvider(new ITreeContentProvider() {
			public void dispose() {	}
			public Object[] getChildren(Object parentElement) {
				return new Object[0];
			}
			public Object[] getElements(Object inputElement) {
				return ((Collection)inputElement).toArray();
			}
			public Object getParent(Object element) {return null;}
			public boolean hasChildren(Object element) { return false; }
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
		});
		viewer.setLabelProvider(provider);
		viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.getTree().setLinesVisible(false);
		viewer.getTree().setSize(300, viewer.getTree().getSize().y);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				if (selection.getFirstElement() instanceof Account) {
					accountSelected((Account)selection.getFirstElement());
				} else if (selection.getFirstElement() instanceof Profile) {
					profileSelected((Profile)selection.getFirstElement());
				}
			}
		});
		return viewer;
	}

	protected void updateData ()
	{
		accounts.setInput(ProfileConfiguration.instance().getAllAccounts());
	}
	
	protected Account currentAccount;
	protected void accountSelected (Account account)
	{
		removeAccount.setEnabled(true);
		editAccount.setEnabled(true);
		
		profiles.setInput(account.getProfiles());
		currentAccount = account;
	}
	
	protected Profile currentProfile;
	protected void profileSelected (Profile profile)
	{
		removeProfile.setEnabled(true);
		
		currentProfile = profile;
	}
	
	protected void removeProfileClicked() {
		if (currentProfile != null)
		{
			currentProfile.getAccount().getProfiles().remove(currentProfile);
			profiles.remove(currentProfile);
		}
	}

	protected void addProfileClicked() {
		if (currentAccount != null)
		{
			
		}
	}

	protected void editAccountClicked() {
		if (currentAccount != null)
		{
			
		}
	}

	protected void removeAccountClicked() {
		if (currentAccount != null)
		{
			ProfileConfiguration.instance().removeAccount(currentAccount);
			accounts.remove(currentAccount);
		}
	}

	protected void addAccountClicked() {
		
	}

}