/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.client.stormfront;

import java.util.Collection;

import com.arcaner.warlock.client.ICompass;
import com.arcaner.warlock.client.IWarlockClient;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IStormFrontClient extends IWarlockClient {
	/**
	 * Returns a list of this client's listeners
	 * @return
	 */
	public Collection<IStormFrontClientListener> getStormFrontClientListeners ();
	
	/**
	 * Add a warlock client listener to this client.
	 * @param listener
	 */
	public void addStormFrontClientListener (IStormFrontClientListener listener);
	
	/**
	 * @return The number of seconds left in the roundtime
	 */
	public int getRoundtime();
	
	/**
	 * Start a new roundtime countdown based on the seconds argument.
	 * @param seconds The number of seconds to count down in the Roundtime bar.
	 * @param label The label to show in the roundtime bar.
	 */
	public void startRoundtime(int seconds, String label);
	
	/**
	 * @return The amount of health in the health bar.
	 */
	public int getHealth();
	
	/**
	 * Set the health bar to this amount, with the supplied label
	 * @param health The amount of health to display in the health bar.
	 * @param label The label the health bar should display
	 */
	public void setHealth (int health, String label);
	
	/**
	 * @return The amount of mana in the mana bar.
	 */
	public int getMana();
	
	/**
	 * Set the mana bar to this amount, with the supplied label
	 * @param mana The amount of mana to display in the mana bar.
	 * @param label The label the mana bar should display
	 */
	public void setMana (int mana, String label);
	
	/**
	 * @return The amount of fatigue in the fatigue bar.
	 */
	public int getFatigue();
	
	/**
	 * Set the fatigue bar to this amount, with the supplied label
	 * @param fatigue The amount of fatigue to display in the fatigue bar.
	 * @param label The label the fatigue bar should display
	 */
	public void setFatigue (int fatigue, String label);
	
	/**
	 * @return The amount of spirit in the spirit bar.
	 */
	public int getSpirit();
	
	/**
	 * Set the spirit bar to this amount, with the supplied label
	 * @param spirit The amount of spirit to display in the spirit bar.
	 * @param label The label the spirit bar should display
	 */
	public void setSpirit (int spirit, String label);
	
	/**
	 * @return The client's compass.
	 */
	public ICompass getCompass();
	
	/**
	 * Call this method immediately after displaying a prompt.
	 */
	public void setPrompting();
	
	/**
	 * @return whether we just showed a prompt or not
	 */
	public boolean isPrompting();
}
