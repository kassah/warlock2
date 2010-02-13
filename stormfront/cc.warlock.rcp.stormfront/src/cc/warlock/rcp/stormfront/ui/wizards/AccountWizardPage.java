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
 * Created on Dec 31, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.stormfront.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import cc.warlock.core.network.IConnection;
import cc.warlock.core.network.ILineConnectionListener;
import cc.warlock.core.network.IConnection.ErrorType;
import cc.warlock.core.profile.Account;
import cc.warlock.core.stormfront.ProfileConfiguration;
import cc.warlock.core.stormfront.network.ISGEGame;
import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.core.stormfront.network.SGEConnectionListener;
import cc.warlock.rcp.stormfront.adapters.SWTSGEConnectionListenerAdapter;
import cc.warlock.rcp.stormfront.ui.util.LoginUtil;
import cc.warlock.rcp.ui.ComboField;
import cc.warlock.rcp.ui.TextField;
import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.ui.network.SWTConnectionListenerAdapter;
import cc.warlock.rcp.wizards.WizardPageWithNotification;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AccountWizardPage extends WizardPageWithNotification implements ILineConnectionListener {

	private SGEConnection connection;
	private ComboField account;
	private TextField password;
	private Listener listener;
	private SWTConnectionListenerAdapter connectionListener;
	private Account savedAccount; 
	
	public AccountWizardPage (SGEConnection connection)
	{
		super (WizardMessages.AccountWizardPage_title, WizardMessages.AccountWizardPage_description,
			WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_WIZBAN_WARLOCK));
		
		this.connection = connection;
		listener = new Listener();
		connectionListener = new SWTConnectionListenerAdapter(this);
		
		connection.addConnectionListener(connectionListener);
		connection.addSGEConnectionListener(new SWTSGEConnectionListenerAdapter(listener));
	}
	
	public void createControl(Composite parent) {
	
		Composite controls = new Composite(parent, SWT.NONE);
		controls.setLayout(new GridLayout(1, false));
		
		new Label(controls, SWT.NONE).setText(WizardMessages.AccountWizardPage_label_accountName);
		account = new ComboField(controls, SWT.BORDER | SWT.DROP_DOWN);
		account.getCombo().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(controls, SWT.NONE).setText(WizardMessages.AccountWizardPage_label_password);
		password = new TextField(controls, SWT.BORDER);
		password.getTextControl().setEchoChar('*');
		password.getTextControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		setControl(controls);
		
		final Collection<Account> accounts = ProfileConfiguration.instance().getAllAccounts();
		for (Account account : accounts) {
			this.account.getCombo().add(account.getAccountName());
		}
		if (accounts.size() > 0)
		{
			account.getCombo().select(0);
			password.getTextControl().setText(ProfileConfiguration.instance().getAccount(account.getCombo().getText()).getPassword());
			
			account.getCombo().addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
				public void widgetSelected(SelectionEvent e) {
					String accountName = account.getCombo().getText();
					password.getTextControl().setText(ProfileConfiguration.instance().getAccount(accountName).getPassword());
				}
			});
		}
	}
	
	protected String accountName;
	
	@Override
	public void pageExited(int button) {
//		if (button == WizardWithNotification.NEXT)
//		{	
			accountName = account.getText();
			savedAccount = ProfileConfiguration.instance().getAccount(account.getText());
			if (savedAccount == null)
			{
				boolean save = MessageDialog.openQuestion(Display.getDefault().getActiveShell(),
					WizardMessages.AccountWizardPage_saveAccount_title, WizardMessages.AccountWizardPage_saveAccount_description);
				
				if (save)
				{
					savedAccount = new Account(account.getText(), password.getText());
					
					ProfileConfiguration.instance().addAccount(savedAccount);
				}
			}
			
			try {
				getContainer().run(false, true, new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException
					{
						listener.setProgressMonitor(monitor);
						
						monitor.beginTask(WizardMessages.bind(WizardMessages.AccountWizardPage_progressMessage, account.getText()), 4);
						if (!connection.isConnected()) {
							connection.connect();
						} else {
							connection.login(account.getText(), password.getText());
						}
						monitor.worked(1);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
//		}
	}
	
	private class Listener extends SGEConnectionListener
	{
		private IProgressMonitor monitor;
		public void setProgressMonitor(IProgressMonitor monitor)
		{
			this.monitor = monitor;
		}
		
		public void loginReady(SGEConnection connection) {
			if (monitor != null) {
				monitor.worked(1);
			}
			
			connection.login(account.getText(), password.getText());
		}
		
		public void loginFinished(SGEConnection connection) {
			if (monitor != null)
			{
				monitor.worked(1);
			}
		}
		
		public void sgeError(SGEConnection connection, int errorCode) {
			LoginUtil.showAuthenticationError(errorCode);
			getContainer().showPage(AccountWizardPage.this);
		}
		
		public void gamesReady(SGEConnection connection, List<? extends ISGEGame> games) {
			if (monitor != null)
			{
				monitor.worked(1);
				monitor.done();
			}
		}
	}
	
	protected Text createTextWithLabel (Composite parent, String label)
	{
		new Label(parent, SWT.NONE).setText(label);
		return  new Text(parent, SWT.BORDER);
	}
	
	public Account getSavedAccount ()
	{
		return savedAccount;
	}
	
	
	public void connectionError(IConnection connection, ErrorType errorType) {
		getWizard().getContainer().showPage(this);
		LoginUtil.showConnectionError(errorType);	
	}
	
	public void connected(IConnection connection) {}
	public void disconnected(IConnection connection) {}
	public void dataReady(IConnection connection, char[] data, int start, int length) {}
	public void lineReady(IConnection connection, String line) {}
}
