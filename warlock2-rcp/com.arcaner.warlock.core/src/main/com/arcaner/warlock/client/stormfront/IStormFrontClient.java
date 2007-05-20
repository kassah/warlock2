/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.client.stormfront;

import com.arcaner.warlock.client.ICompass;
import com.arcaner.warlock.client.IProperty;
import com.arcaner.warlock.client.IWarlockStyle;
import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.configuration.server.ServerSettings;
import com.arcaner.warlock.script.IScriptCommands;

/**
 * @author Marshall
 */
public interface IStormFrontClient extends IWarlockClient {
	
	public static final String DEATH_STREAM_NAME = "death";
	
	public static final String INVENTORY_STREAM_NAME = "inv";
	
	public static final String THOUGHTS_STREAM_NAME = "thoughts";
	
	/**
	 * The server settings for this client
	 * @return
	 */
	public ServerSettings getServerSettings();
	
	/**
	 * @return The player ID of the current player
	 */
	public IProperty<String> getPlayerId();
	
	/**
	 * @return The roundtime property
	 */
	public IProperty<Integer> getRoundtime();
	
	/**
	 * Start a new roundtime countdown based on the seconds argument.
	 * @param seconds The number of seconds to count down in the Roundtime bar.
	 * @param label The label to show in the roundtime bar.
	 */
	public void startRoundtime(int seconds);
	
	public void updateRoundtime(int secondsLeft);
	
	/**
	 * @return The health property
	 */
	public IProperty<Integer> getHealth();	
	
	/**
	 * @return The amount of mana in the mana bar.
	 */
	public IProperty<Integer >getMana();
	
	/**
	 * @return The amount of fatigue in the fatigue bar.
	 */
	public IProperty<Integer> getFatigue();
	
	/**
	 * @return The amount of spirit in the spirit bar.
	 */
	public IProperty<Integer> getSpirit();
	
	/**
	 * @return The client's compass.
	 */
	public ICompass getCompass();
	
	/**
	 * Sets the current style of the storm front client
	 */
	public void setCurrentStyle (IWarlockStyle style);
	
	/**
	 * @return The current style of the storm front client
	 */
	public IWarlockStyle getCurrentStyle();

	/**
	 * @return The script commands interface that corresponds to this client
	 */
	public IScriptCommands getScriptCommands ();
	
	/**
	 * @return The name of the character associated with this client.
	 */
	public IProperty<String> getCharacterName();
}
