package cc.warlock.rcp.stormfront.ui.wizards;

import java.io.File;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import cc.warlock.core.configuration.Profile;
import cc.warlock.core.stormfront.ProfileConfiguration;
import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.wizards.WizardPageWithNotification;

class ImportSettingsWizardPage extends WizardPageWithNotification
{
	protected TableViewer profileTable;
	protected Button serverButton;
	protected Button fileButton;
	private Group grpSelectFileTo;
	private Text fileText;
	private Button browseFilesButton;
	private Label lblImportFile;
	private Label lblImportServer;
	private Composite composite;
	public static final int SERVER_IMPORT = 1;
	public static final int FILE_IMPORT = 2;
	
	public ImportSettingsWizardPage ()
	{
		super("Import Stormfront Settings", "Import Stormfront Settings",
			WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_WIZBAN_WARLOCK));
		
		setDescription("Configure Import of Settings.");
	}
	
	public void createControl(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		setControl(main);
		main.setLayout(new GridLayout(2, false));
		
		serverButton = new Button(main, SWT.RADIO);
		serverButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (serverButton.getSelection()) {
					setErrorMessage(null);
					profileTable.getTable().deselectAll();
					profileTable.getTable().setEnabled(true);
				}
			}
		});
		serverButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		//serverButton.setText("Import settings from server for currently connected character.");
		
		lblImportServer = new Label(main, SWT.NONE);
		GridData gd_lblImportServer = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblImportServer.horizontalIndent = 5;
		lblImportServer.setLayoutData(gd_lblImportServer);
		lblImportServer.setText("Import settings from server.");
		//new Label(main, SWT.NONE);
		
		fileButton = new Button(main, SWT.RADIO);
		fileButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		fileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (fileButton.getSelection()) {
					profileTable.getTable().setEnabled(false);
					profileTable.getTable().deselectAll();
					fileText.setText("");
					fileText.setEnabled(true);
					browseFilesButton.setEnabled(true);
				} else {
					fileText.setEnabled(false);
					browseFilesButton.setEnabled(false);
				}
			}
		});
		
		composite = new Composite(main, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(1,false));
		lblImportFile = new Label(composite, SWT.NONE);
		lblImportFile.setText("Import settings from StormFront save or export file.");
		new Label(main, SWT.NONE);
		//new Label(main, SWT.NONE);
		//fileButton.setText("Import settings from StormFront save or export file.");
		
		grpSelectFileTo = new Group(composite, SWT.NONE);
		grpSelectFileTo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSelectFileTo.setText("Path to Stormfront settings xml file: ");
		grpSelectFileTo.setLayout(new GridLayout(3, false));
		
		fileText = new Text(grpSelectFileTo, SWT.BORDER);
		fileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		fileText.setEnabled(false);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fileTextModified();
			}
		});
		
		browseFilesButton = new Button(grpSelectFileTo, SWT.NONE);
		browseFilesButton.setText("Browse");
		browseFilesButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				browseFilesSelected();
			}
		});
		browseFilesButton.setEnabled(false);
		new Label(grpSelectFileTo, SWT.NONE);
		new Label(main, SWT.NONE);
		
		Composite profileComposite = new Composite(main, SWT.NONE);
		profileComposite.setLayout(new GridLayout(1, false));
		GridData gd_profileComposite = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_profileComposite.horizontalSpan = 2;
		profileComposite.setLayoutData(gd_profileComposite);
		
		new Label(profileComposite, SWT.NONE).setText("Profile to import settings into:");
		
		profileTable = new TableViewer(profileComposite, SWT.BORDER);
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
	}
	
	protected void browseFilesSelected ()
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
			fileText.setText(path);
		}
	}
	
	protected void fileTextModified ()
	{
		setPageComplete(false);
		File file = new File(fileText.getText());
		
		if (!file.exists()) {
			setErrorMessage("File does not exist");
			profileTable.getTable().setEnabled(false);
			profileTable.getTable().deselectAll();
		} else {
			setErrorMessage(null);
			profileTable.getTable().setEnabled(true);
			
			if (!profileTable.getSelection().isEmpty()) {
				setPageComplete(true);
			}
		}
	}
	
	public int getType() {
		if (serverButton.getSelection())
			return ImportSettingsWizardPage.SERVER_IMPORT;
		if (fileButton.getSelection())
			return ImportSettingsWizardPage.FILE_IMPORT;
		return 0;
	}
	
	public String getFile() {
		return fileText.getText();
	}
	
	public Profile getTargetProfile() {
		return (Profile) ((IStructuredSelection)profileTable.getSelection()).getFirstElement();
	}
	
	@Override
	public boolean isPageComplete() {
		if (!profileTable.getSelection().isEmpty()) {
			if (serverButton.getSelection())
				return true;
			if (fileButton.getSelection()) {
				return (fileText.getText() != null && fileText.getText().length() > 0);
				// Check the file button stuff.
			}
		}
		return false;
	}
}