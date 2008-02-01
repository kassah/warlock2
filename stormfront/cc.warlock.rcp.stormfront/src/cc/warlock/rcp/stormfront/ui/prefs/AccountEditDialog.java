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
package cc.warlock.rcp.stormfront.ui.prefs;

import java.io.IOException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.core.stormfront.network.SGEConnectionListener;
import cc.warlock.rcp.stormfront.ui.util.LoginUtil;
import cc.warlock.rcp.ui.TextField;

public class AccountEditDialog extends Dialog {

	protected static int TEST_ACCOUNT = 3;
	protected String username, password;
	protected TextField usernameField;
	protected TextField passwordField;
	protected Label statusLabel;
	
	public AccountEditDialog (Shell parentShell)
	{
		super(parentShell);
	}
	
	public AccountEditDialog (Shell parentShell, String username, String password)
	{
		this(parentShell);
		
		this.username = username;
		this.password = password;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		if (username == null)
			newShell.setText("Add a new account");
		else
			newShell.setText("Update an existing account");
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(2, false));
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label banner = new Label(main, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.horizontalSpan = 2;
		banner.setLayoutData(data);
		if (username == null) {
			banner.setText("Add a new account");
		} else {
			banner.setText("Update an existing account");
		}
		banner.setFont(JFaceResources.getHeaderFont());
		
		new Label(main, SWT.NONE).setText("Username:");
		usernameField = new TextField(main, SWT.BORDER);
		usernameField.getTextControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		ModifyListener listener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (getButton(OK) != null)
				{
					getButton(OK).setEnabled(false);
					statusLabel.setText("");
				}
			}
		};
		usernameField.getTextControl().addModifyListener(listener);
		
		if (username != null)
			usernameField.getTextControl().setText(username);
		
		new Label(main, SWT.NONE).setText("Password:");
		passwordField = new TextField(main, SWT.BORDER);
		passwordField.getTextControl().setEchoChar('*');
		passwordField.getTextControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		passwordField.getTextControl().addModifyListener(listener);
		if (password != null)
			passwordField.getTextControl().setText(password);
		
		statusLabel = new Label(main, SWT.NONE);
		data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.horizontalSpan = 2;
		statusLabel.setLayoutData(data);
//		statusLabel.setFont(JFaceResources.getBannerFont());
		
		return main;
	}
	
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, TEST_ACCOUNT, "Test Account", true);
		createButton(parent, OK, "OK", false);
		createButton(parent, CANCEL, "Cancel", false);
		
		if (username == null)
			getButton(OK).setEnabled(false);
	}
	
	protected static class AccountVerifier extends SGEConnectionListener
	{
		protected String username, password;
		protected boolean verified, verifying;
		protected int errorCode;
		
		public AccountVerifier (String username, String password)
		{
			this.username = username;
			this.password = password;
			verified = false;
			verifying = true;
			
			SGEConnection connection = new SGEConnection();
			connection.setRetrieveGameInfo(false);
			connection.addSGEConnectionListener(this);
			connection.connect();
		}
		
		@Override
		public void loginReady(SGEConnection connection) {
			connection.login(username, password);
		}
		
		@Override
		public void sgeError(SGEConnection connection, int errorCode) {
			this.errorCode = errorCode;
			verified = false;
			verifying = false;
			
			try {
				connection.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void loginFinished(SGEConnection connection) {
			verified = true;
			verifying = false;
			
			try {
				connection.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public boolean verify ()
		{
			while (verifying)
			{
				try {
					Thread.sleep((long) 300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return verified;
		}

		public int getErrorCode() {
			return errorCode;
		}
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == TEST_ACCOUNT)
		{
			AccountVerifier verifier = new AccountVerifier(usernameField.getText(), passwordField.getText());
			
			boolean verified = verifier.verify();
			if (!verified) {
				statusLabel.setBackground(JFaceColors.getErrorBackground(getShell().getDisplay()));
				statusLabel.setForeground(JFaceColors.getErrorText(getShell().getDisplay()));
				String loginError = LoginUtil.getAuthenticationError(verifier.getErrorCode());
				statusLabel.setText(loginError);
				
				getButton(OK).setEnabled(false);
			}
			else {
				statusLabel.setBackground(getShell().getBackground());
				statusLabel.setForeground(new Color(getShell().getDisplay(), 0, 128, 0));
				statusLabel.setText("Succesfully logged in to the server");
				
				getButton(OK).setEnabled(true);
			}
		}
		else
		{
			super.buttonPressed(buttonId);
		}
	}
	
	public String getUsername ()
	{
		return usernameField.getText();
	}
	
	public String getPassword ()
	{
		return passwordField.getText();
	}
}
