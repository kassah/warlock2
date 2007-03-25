package com.arcaner.warlock.rcp.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.rcp.views.GameView;

public class Entry extends Text {

	protected GameView gameView;
	
	public Entry(GameView gameView, Composite parent, int style) {
		super(parent, style);
		
		this.gameView = gameView;
	}
	
	public void submit() {
		IWarlockClient client = gameView.getClient();
		String toSend = getText();
		if (toSend != null && toSend.length() > 0) {
			client.send (toSend);
			client.echo (toSend);
		}
		
		setText("");
	}
}
