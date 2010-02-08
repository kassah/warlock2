package cc.warlock.rcp.telnet.ui.views;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.settings.IClientSettings;
import cc.warlock.rcp.telnet.core.client.TelnetClient;
import cc.warlock.rcp.ui.client.SWTWarlockClientViewer;
import cc.warlock.rcp.views.GameView;

public class TelnetGameView extends GameView {

	public static final String VIEW_ID = "cc.warlock.rcp.telnet.ui.views.TelnetGameView";
	
	protected TelnetClient telnetClient;
	
	public TelnetGameView ()
	{
		super();
		
		wrapper = new SWTWarlockClientViewer(this);
		//setBufferOnPrompt(false);
		//setAppendNewlines(false);
	}
	
	@Override
	public void setClient(IWarlockClient client) {
		super.setClient(client);
		
		if (client instanceof TelnetClient) {
			telnetClient = (TelnetClient) client;
		}
	}
	
	public void loadClientSettings(IClientSettings settings) {
	
	}

	public TelnetClient getTelnetClient() {
		return telnetClient;
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		if (TelnetClient.class.equals(adapter))
		{
			return telnetClient;
		}
		return super.getAdapter(adapter);
	}

	@Override
	protected void setViewTitle(String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clientActivated(IWarlockClient client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clientConnected(IWarlockClient client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clientDisconnected(IWarlockClient client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clientRemoved(IWarlockClient client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clientSettingsLoaded(IWarlockClient client) {
		// TODO Auto-generated method stub
		
	}
}
