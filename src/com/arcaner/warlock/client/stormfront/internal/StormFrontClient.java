/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.client.stormfront.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.arcaner.warlock.client.ICompass;
import com.arcaner.warlock.client.IWarlockClientViewer;
import com.arcaner.warlock.client.internal.Compass;
import com.arcaner.warlock.client.internal.WarlockClient;
import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.client.stormfront.IStormFrontClientListener;
import com.arcaner.warlock.network.StormFrontConnection;
import com.arcaner.warlock.views.CompassView;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StormFrontClient extends WarlockClient implements IStormFrontClient {

	protected ICompass compass;
	protected CompassView compassView;
	protected int roundtime, health, mana, fatigue, spirit, lastPrompt;
	protected ArrayList<IStormFrontClientListener> clientListeners;
	protected boolean isPrompting = false;
	protected StringBuffer buffer = new StringBuffer();
	protected IStormFrontProtocolHandler handler;
	
	public StormFrontClient(IWarlockClientViewer viewer) {
		super(viewer);
		
		compass = new Compass(this);
		compassView = new CompassView(this);
		compass.addListener(compassView);
		
		roundtime = health = mana = fatigue = spirit = 0;
		clientListeners = new ArrayList<IStormFrontClientListener>();
	}
	
	public void output(String viewName, String text) {
		
		if(viewName == null) {
			boolean flush = false;
			
			// if we've already shown the prompt newline, don't do it again
			if(isPrompting) {
				if(text.charAt(0) == '\n') {
					text = text.substring(1);
					flush = true;
				} else if(text.startsWith("\r\n")) {
					text = text.substring(2);
					flush = true;
				}
			}
			
			// search for a newline
			int end = text.lastIndexOf('\r');
			if(end < 0) end = text.lastIndexOf('\n');
			if(end >= 0) {
				flush = true;
			} else {
				// if there was no newline, the end is the start
				end = 0;
			}
			
			/*
			 * if there was a newline, output the existing buffer, and then the
			 * text up to that new line
			 */
			if(flush) {
				// if there is text to output, add it to the buffer
				if(end > 0) {
					// when we finish prompting, prepend a newline
					if(isPrompting) {
						isPrompting = false;
						buffer.append("\r\n");
					}
					buffer.append(text, 0, end);
				}
				
				/*
				 * it's possible that our end was the first character, so the
				 * buffer might still be empty, we need to check it.
				 */
				// if there is something in the buffer, output and clear it
				if(buffer.length() > 0) {
					super.output(viewName, buffer.toString());
					buffer.delete(0, buffer.length());
				}
			}
			
			// append the rest of the text to the buffer
			if(text.length() > end) {
				// when we finish prompting, prepend a newline
				if(isPrompting) {
					isPrompting = false;
					buffer.append("\r\n");
				}
				buffer.append(text, end, text.length());
			}
		} else {
			System.out.println("Got a stream not shown of id: " + viewName);
		}
	}
	
	public Collection<IStormFrontClientListener> getStormFrontClientListeners() {
		return clientListeners;
	}

	public void addStormFrontClientListener(IStormFrontClientListener listener) {
		clientListeners.add(listener);
	}
	
	public void removeStormFrontClientListener(IStormFrontClientListener listener) {
		clientListeners.remove(listener);
	}

	public int getRoundtime() {
		return roundtime;
	}

	private void startRoundtime (int seconds)
	{
		roundtime = seconds;
		for (IStormFrontClientListener listener : clientListeners) {
			listener.roundtimeStarted(this, seconds);
		}
	}
	
	private void updateRoundtimes (int currentRoundtime)
	{
		roundtime = currentRoundtime;
		for (IStormFrontClientListener listener : clientListeners) {
			listener.roundtimeChanged(this, currentRoundtime);
		}
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
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health, String label) {
		this.health = health;
		for (IStormFrontClientListener listener : clientListeners) {
			listener.healthChanged(this, health, label);
		}
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana, String label) {
		this.mana = mana;
		for (IStormFrontClientListener listener : clientListeners) {
			listener.manaChanged(this, mana, label);
		}
	}

	public int getFatigue() {
		return fatigue;
	}

	public void setFatigue(int fatigue, String label) {
		this.fatigue = fatigue;
		for (IStormFrontClientListener listener : clientListeners) {
			listener.fatigueChanged(this, fatigue, label);
		}
	}

	public int getSpirit() {
		return spirit;
	}

	public void setSpirit(int spirit, String label) {
		this.spirit = spirit;
		for (IStormFrontClientListener listener : clientListeners) {
			listener.spiritChanged(this, spirit, label);
		}
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
	
	public void setPrompting() {
		isPrompting = true;
	}
	
	public boolean isPrompting() {
		return isPrompting;
	}
	
}
