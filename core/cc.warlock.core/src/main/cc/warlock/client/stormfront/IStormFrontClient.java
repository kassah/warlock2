/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.client.stormfront;

import java.util.List;

import cc.warlock.client.ICompass;
import cc.warlock.client.IProperty;
import cc.warlock.client.IWarlockClient;
import cc.warlock.client.IWarlockStyle;
import cc.warlock.configuration.server.ServerSettings;
import cc.warlock.script.IScript;
import cc.warlock.script.IScriptCommands;
import cc.warlock.script.IScriptListener;

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
	 * @return The left hand property
	 */
	public IProperty<String> getLeftHand();
	
	/**
	 * @return The right hand property
	 */
	public IProperty<String> getRightHand();
	
	/**
	 * @return The current spell property
	 */
	public IProperty<String> getCurrentSpell();
	
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
	
	/**
	 * @return A list of currently running scripts
	 */
	public List<IScript> getRunningScripts();
	
	/**
	 * Add a script listener
	 * @param listener
	 */
	public void addScriptListener (IScriptListener listener);
	
	public void removeScriptListener (IScriptListener listener);
}
