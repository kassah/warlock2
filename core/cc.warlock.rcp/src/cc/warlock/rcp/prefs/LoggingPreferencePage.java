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
package cc.warlock.rcp.prefs;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.logging.LoggingConfiguration;
import cc.warlock.core.client.settings.internal.ClientSettings;

/**
 * @author Will Robertson
 * Logging Preferences Page
 */
public class LoggingPreferencePage extends PreferencePageUtils implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.prefs.logging";
	
	protected ClientSettings settings;
	protected IWarlockClient client;
	protected Combo loggingType;
	protected Text logDir;
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(2, false));
		
		// Logging Output Type (text/html)
		Label loggingTypeLabel = new Label(main, SWT.NONE);
		loggingTypeLabel.setText("Logging Output Type: ");
		loggingType = new Combo(main, SWT.BORDER | SWT.READ_ONLY);
		loggingType.setItems(new String[] {
			LoggingConfiguration.LOG_FORMAT_TEXT,
			LoggingConfiguration.LOG_FORMAT_HTML
		});
		loggingType.setText(LoggingConfiguration.instance().getLogFormat());
		
		// Log To Directory
		new Label(main, SWT.NONE).setText("Log Directory:");
		Composite logDirComposite = new Composite(main, SWT.NONE);
		logDirComposite.setLayout(new GridLayout(2, false));
		logDir = new Text(logDirComposite, SWT.BORDER | SWT.SINGLE);
		logDir.setText(LoggingConfiguration.instance().getLogDirectory().getAbsolutePath());
		Button logDirButton = new Button(logDirComposite, SWT.PUSH);
		logDirButton.setText("Browse");
		logDirButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setDirectoryClicked();
			}
		});
		
		// Return the main composite
		return main;
	}
	
	/*
	 * Opens up DirectoryDialog and saves the result into the logDir button.
	 */
	protected void setDirectoryClicked ()
	{
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		String directory = dialog.open();
		
		if (directory != null)
		{
			File dir = new File(directory);
			logDir.setText(dir.getAbsolutePath());
		}
	}
	
	protected void performDefaults() {
        super.performDefaults();
        // TODO: Stub, we don't currently have a way to get default settings.
    }

	@Override
	public boolean performOk() {
		LoggingConfiguration.instance().setLogFormat(loggingType.getText());
		LoggingConfiguration.instance().setLogDirectory(new File(logDir.getText()));
		return true;
	}
}
