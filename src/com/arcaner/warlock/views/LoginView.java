
package com.arcaner.warlock.views;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

import com.arcaner.warlock.network.ISGEConnectionListener;
import com.arcaner.warlock.network.SGEConnection;
import com.arcaner.warlock.network.SWTSGEConnectionListenerAdapter;

/**
 * @author Marshall
 */
public class LoginView extends ViewPart implements ISGEConnectionListener {

	protected Tree actionTree;
	protected Text account, password;
	protected List game;
	protected Button login;
	protected SGEConnection sgeConnection;
	protected Map<String, String> games, characters;
	
	public void createPartControl(Composite parent) {

		sgeConnection = new SGEConnection();
		sgeConnection.addSGEConnectionListener(new SWTSGEConnectionListenerAdapter(this));
		
		Composite top = new Composite (parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		top.setLayout(layout);
		
		actionTree = new Tree(top, SWT.BORDER);
		actionTree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		Composite controls = new Composite(top, SWT.NONE);
		controls.setLayout(new GridLayout(2, false));
		
		account = createTextWithLabel(controls, "Account Name");
		password = createTextWithLabel(controls, "Password");
		password.setEchoChar('*');
		game = createListWithLabel(controls, "Select a Game");
		game.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected (SelectionEvent e) {
				widgetSelected (e);
			}
			public void widgetSelected (SelectionEvent e) {
				gameSelected();
			}
		});
		
		login = new Button(controls, SWT.PUSH);
		login.setText("Login");
		login.setEnabled(false);
		
		login.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				loginClicked();
			}
		});
	}

	protected Text createTextWithLabel (Composite parent, String label)
	{
		new Label(parent, SWT.NONE).setText(label);
		return  new Text(parent, SWT.BORDER);
	}
	
	protected List createListWithLabel (Composite parent, String label)
	{
		new Label(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER).setText(label);
		return new List(parent, SWT.BORDER);
	}
	
	protected void loginClicked ()
	{
		sgeConnection.login(account.getText(), password.getText());
	}
	
	protected void gameSelected ()
	{
		String selected = game.getSelection()[0];
		for (String gameCode : games.keySet()) {
			String gameDesc = (String) games.get(gameCode);
			if (gameDesc.equals(selected))
			{
				sgeConnection.selectGame(gameCode);
				break;
			}
		}
	}
	
	public void setFocus() {
	}

	public void loginReady(SGEConnection connection) {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run () {
				login.setEnabled(true);
			}
		});
	}

	public void loginFinished(SGEConnection connection, int status) {
		System.out.println("login finished, status: " + status);
	}
	
	public void gamesReady(SGEConnection connection, Map<String, String> games)
	{
		this.games = games;
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run () {
				for (String gameDesc : LoginView.this.games.values()) {
					game.add(gameDesc);
				}
			}
		});
	}
	
	public void charactersReady(SGEConnection connection, Map<String, String> characters)
	{
		this.characters = characters;
		/*getSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run () {
				for (Iterator keys = LoginView.this.characters.keySet().iterator(); keys.hasNext(); )
				{
					String gameDesc = (String) LoginView.this.characters.get(keys.next());
					
				}
			}
		});*/
	}
	
	/* (non-Javadoc)
	 * @see com.arcaner.warlock.network.ISGEConnectionListener#readyToPlay(com.arcaner.warlock.network.SGEConnection, java.util.Map)
	 */
	public void readyToPlay(SGEConnection connection, Map loginProperties) {
		// TODO Auto-generated method stub

	}
}
