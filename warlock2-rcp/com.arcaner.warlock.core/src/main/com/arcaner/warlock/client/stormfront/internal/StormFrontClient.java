/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.client.stormfront.internal;

import java.io.IOException;

import com.arcaner.warlock.client.ICompass;
import com.arcaner.warlock.client.IProperty;
import com.arcaner.warlock.client.IWarlockStyle;
import com.arcaner.warlock.client.internal.ClientProperty;
import com.arcaner.warlock.client.internal.Compass;
import com.arcaner.warlock.client.internal.WarlockStyle;
import com.arcaner.warlock.client.internal.Stream;
import com.arcaner.warlock.client.internal.WarlockClient;
import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.configuration.ServerSettings;
import com.arcaner.warlock.network.StormFrontConnection;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StormFrontClient extends WarlockClient implements IStormFrontClient {

	protected ICompass compass;
	protected int lastPrompt;
	protected ClientProperty<Integer> roundtime, health, mana, fatigue, spirit;
	protected boolean isPrompting = false;
	protected StringBuffer buffer = new StringBuffer();
	protected IStormFrontProtocolHandler handler;
	protected boolean isBold;
	protected ClientProperty<String> playerId;
	protected IWarlockStyle currentStyle = WarlockStyle.EMPTY_STYLE;
	protected ServerSettings serverSettings;
	
	public StormFrontClient() {
		compass = new Compass(this);
		
		roundtime = new ClientProperty<Integer>(this, "roundtime");
		health = new ClientProperty<Integer>(this, "health");
		mana = new ClientProperty<Integer>(this, "mana");
		fatigue = new ClientProperty<Integer>(this, "fatigue");
		spirit = new ClientProperty<Integer>(this, "spirit");
		playerId = new ClientProperty<String>(this, "playerId");
		serverSettings = new ServerSettings(this);
	}

	public IProperty<Integer> getRoundtime() {
		return roundtime;
	}

	private void startRoundtime (int seconds)
	{
		roundtime.activate();
		roundtime.set(seconds);
	}
	
	private void updateRoundtimes (int currentRoundtime)
	{
		roundtime.set(currentRoundtime);
	}
	
	public void startRoundtime (final int seconds, String label) {
		new Thread (new Runnable () {
			private int elapsed = 0;
			
			public void run() {
				try {
					startRoundtime(seconds);
					
					while (elapsed < seconds)
					{
						updateRoundtimes(seconds - elapsed);
						
						elapsed++;
						Thread.sleep((long)1000);
					}
					
					updateRoundtimes(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public IProperty<Integer> getHealth() {
		return health;
	}

	public IProperty<Integer> getMana() {
		return mana;
	}

	public IProperty<Integer> getFatigue() {
		return fatigue;
	}

	public IProperty<Integer> getSpirit() {
		return spirit;
	}

	public ICompass getCompass() {
		return compass;
	}
	
	public void connect(String server, int port, String key) throws IOException {
		try {
			connection = new StormFrontConnection(this, key);
			connection.connect(server, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void streamCleared() {
		// TODO Auto-generated method stub
		
	}
	
	public void setPrompting() {
		isPrompting = true;
	}
	
	public boolean isPrompting() {
		return isPrompting;
	}
	
	public void setBold(boolean bold) {
		isBold = bold;
	}
	
	public boolean isBold() {
		return isBold;
	}

	public IWarlockStyle getCurrentStyle() {
		return currentStyle;
	}

	public void setCurrentStyle(IWarlockStyle currentStyle) {
		this.currentStyle = currentStyle;
	}

	public ClientProperty<String> getPlayerId() {
		return playerId;
	}
	
	public ServerSettings getServerSettings() {
		return serverSettings;
	}
}
