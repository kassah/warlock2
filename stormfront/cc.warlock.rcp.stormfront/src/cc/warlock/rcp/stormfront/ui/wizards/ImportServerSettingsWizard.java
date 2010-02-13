package cc.warlock.rcp.stormfront.ui.wizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.profile.Profile;
import cc.warlock.core.stormfront.ProfileConfiguration;
import cc.warlock.core.stormfront.client.internal.StormFrontClient;
import cc.warlock.core.stormfront.settings.StormFrontServerSettings;
import cc.warlock.core.stormfront.settings.internal.StormFrontClientSettings;
import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.wizards.WizardPageWithNotification;
import cc.warlock.rcp.wizards.WizardWithNotification;

public class ImportServerSettingsWizard extends WizardWithNotification implements IImportWizard {

	protected PathWizardPage page1;

	@Override
	public void addPages() {
		page1 = new PathWizardPage();
		addPage(page1);
	}
	
	@Override
	public boolean performFinish() {
		String settingsPath = page1.settingsPathText.getText();
		Profile profile = (Profile) ((IStructuredSelection)page1.profileTable.getSelection()).getFirstElement();
		
		StormFrontServerSettings serverSettings = null;
		StormFrontClientSettings clientSettings = null;
		
		for (IWarlockClient client : WarlockClientRegistry.getActiveClients()) 
		{
			if (client instanceof StormFrontClient) {
				StormFrontClient sfClient = (StormFrontClient) client;
				
				if (sfClient.getCharacterName().get().equals(profile.getName())) {
					clientSettings = (StormFrontClientSettings) sfClient.getStormFrontClientSettings();
					serverSettings = sfClient.getServerSettings();
				}
			}
		}
		
		if (clientSettings != null && serverSettings != null) {
			try {
				FileInputStream stream = new FileInputStream(new File(settingsPath));
				serverSettings.importServerSettings(stream, clientSettings);
				
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {}

	protected class PathWizardPage extends WizardPageWithNotification
	{
		protected Text settingsPathText;
		protected TableViewer profileTable;
		
		public PathWizardPage ()
		{
			super("Import Stormfront Settings", "Import Stormfront Settings",
				WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_WIZBAN_WARLOCK));
			
			setDescription("Import settings either exported or saved from Stormfront.");
		}
		
		public void createControl(Composite parent) {
			Composite main = new Composite(parent, SWT.NONE);
			main.setLayout(new GridLayout(1, false));
			main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			new Label(main, SWT.NONE).setText("Path to Stormfront settings xml file:");
			
			Composite pathComposite = new Composite(main, SWT.NONE);
			pathComposite.setLayout(new GridLayout(2, false));
			pathComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			
			settingsPathText = new Text(pathComposite, SWT.BORDER | SWT.SINGLE);
			settingsPathText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					pathModified();
				}
			});
			settingsPathText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			
			Button browseButton = new Button(pathComposite, SWT.PUSH);
			browseButton.setText("Browse...");
			browseButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					browseSelected();
				}
			});
			
			new Label(main, SWT.NONE).setText("Profile to import settings into:");
			
			profileTable = new TableViewer(main, SWT.BORDER);
			profileTable.setContentProvider(new ArrayContentProvider());
			profileTable.setLabelProvider(new ILabelProvider () {
				public void addListener(ILabelProviderListener listener) {	}
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
			profileTable.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					setPageComplete(!profileTable.getSelection().isEmpty());
				}
			});
			
			profileTable.setInput(ProfileConfiguration.instance().getAllProfiles());
			profileTable.getTable().setEnabled(false);
			profileTable.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			setControl(main);
		}
		
		
		protected void pathModified ()
		{
			setPageComplete(false);
			File file = new File(settingsPathText.getText());
			
			if (!file.exists()) {
				setErrorMessage("File does not exist");
				profileTable.getTable().setEnabled(false);
			} else {
				setErrorMessage(null);
				profileTable.getTable().setEnabled(true);
				
				if (!profileTable.getSelection().isEmpty()) {
					setPageComplete(true);
				}
			}
		}
		
		protected void browseSelected ()
		{
			FileDialog dialog = new FileDialog(getShell());
			
			if (Platform.getOS().equals(Platform.OS_WIN32))
			{
				File appDir = new File(System.getenv("AppData"));
				File stormFrontDir = new File(appDir, "StormFront");
				
				if (stormFrontDir.exists()) {
					dialog.setFilterPath(stormFrontDir.getAbsolutePath());
				}
			}
			
			String path = dialog.open();
			if (path != null) {
				settingsPathText.setText(path);
			}
		}
		
		@Override
		public boolean isPageComplete() {
			return (settingsPathText.getText() != null && settingsPathText.getText().length() > 0)
				&& !profileTable.getSelection().isEmpty();
		}
	}
}
