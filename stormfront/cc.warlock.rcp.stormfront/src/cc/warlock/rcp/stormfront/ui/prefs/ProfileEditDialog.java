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
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import cc.warlock.core.stormfront.network.ISGEGame;
import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.core.stormfront.network.SGEConnectionListener;
import cc.warlock.core.stormfront.profile.StormFrontAccount;
import cc.warlock.core.stormfront.profile.StormFrontProfile;
import cc.warlock.rcp.ui.ComboField;

public class ProfileEditDialog extends Dialog {

	protected StormFrontAccount account;
	protected StormFrontProfile profile;
	
	protected ComboField gameField;
	protected ComboField characterField;
	protected Label statusLabel;
	
	public ProfileEditDialog (Shell parentShell, StormFrontAccount account)
	{
		super(parentShell);
		
		this.account = account;
	}
	
	public ProfileEditDialog (Shell parentShell, StormFrontAccount account, StormFrontProfile profile)
	{
		this(parentShell, account);
		
		this.profile = profile;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		if (profile == null)
			newShell.setText("Add a new profile");
		else
			newShell.setText("Update an existing profile");
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
		if (profile == null) {
			banner.setText("Add a new profile");
		} else {
			banner.setText("Update an existing profile");
		}
		banner.setFont(JFaceResources.getHeaderFont());
		
		new Label(main, SWT.NONE).setText("Game:");
		gameField = new ComboField(main, SWT.BORDER);
		gameField.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		SelectionListener listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (getButton(OK) != null)
				{
					getButton(OK).setEnabled(false);
					statusLabel.setText("");
				}
			}
		};
		gameField.getCombo().addSelectionListener(listener);
		
		if (profile != null)
			gameField.getCombo().setText(profile.getGameName());
		
		new Label(main, SWT.NONE).setText("Password:");
		characterField = new ComboField(main, SWT.BORDER);
		characterField.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		if (profile != null)
			characterField.getCombo().setText(profile.getName());
		
		statusLabel = new Label(main, SWT.NONE);
		data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.horizontalSpan = 2;
		statusLabel.setLayoutData(data);
		
		updateData();
		return main;
	}
	
	protected SGEConnection connection;
	protected void updateData()
	{
		
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, OK, "OK", true);
		createButton(parent, CANCEL, "Cancel", false);
		
		if (profile == null)
		{
			getButton(OK).setEnabled(false);
		}
	}
	
	protected static class ProfileRetriever extends SGEConnectionListener
	{
		protected StormFrontAccount account;
		protected boolean loggedIn, gettingCharacters;
		protected int errorCode = -1;
		protected List<? extends ISGEGame> games;
		protected Map<String, String> characters;
		protected SGEConnection connection;
		
		public ProfileRetriever (StormFrontAccount account)
		{
			this.account = account;
			loggedIn = false;
			
			connection = new SGEConnection();
			connection.setRetrieveGameInfo(false);
			connection.addSGEConnectionListener(this);
			connection.connect();
		}
		
		@Override
		public void loginReady(SGEConnection connection) {
			connection.login(account.getAccountName(), account.getPassword());
		}
		
		@Override
		public void gamesReady(SGEConnection connection, List<? extends ISGEGame> games) {
			this.games = games;
			loggedIn = true;
		}
		
		@Override
		public void charactersReady(SGEConnection connection, Map<String, String> characters) {
			this.characters = characters;
			
			gettingCharacters = false;
		}
		
		@Override
		public void sgeError(SGEConnection connection, int errorCode) {
			this.errorCode = errorCode;
			loggedIn = false;
			
			try {
				connection.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public List<? extends ISGEGame> getGames() {
			while (!loggedIn && errorCode != -1)
			{
				try {
					Thread.sleep((long)300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return games;
		}
		
		public int getErrorCode () {
			return errorCode;
		}
		
		public Map<String, String> getCharacters (ISGEGame game) {
			gettingCharacters = true;
			connection.selectGame(game.getGameCode());
			
			while (gettingCharacters && errorCode == -1)
			{
				try {
					Thread.sleep((long)300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return characters;
		}
	}
	
//	public ISGEGame getGame ()
//	{
//		return game;
//	}
//	
//	public String getCharacterName ()
//	{
//		return characterName;
//	}
//	
//	public String getCharacterCode ()
//	{
//		return characterCode;
//	}
}
