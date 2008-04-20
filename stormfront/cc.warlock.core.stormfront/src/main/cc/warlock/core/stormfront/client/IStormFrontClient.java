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
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.stormfront.client;

import java.util.Collection;

import cc.warlock.core.client.ICharacterStatus;
import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptListener;
import cc.warlock.core.stormfront.settings.IStormFrontClientSettings;
import cc.warlock.core.stormfront.settings.skin.IStormFrontSkin;

/**
 * @author Marshall
 */
public interface IStormFrontClient extends IWarlockClient, IRoomListener {
	
	public static final String DEATH_STREAM_NAME = "death";
	public static final String INVENTORY_STREAM_NAME = "inv";
	public static final String THOUGHTS_STREAM_NAME = "thoughts";
	public static final String ROOM_STREAM_NAME = "room";
	public static final String FAMILIAR_STREAM_NAME = "familiar";
	
	public static final String COMPONENT_ROOM_PLAYERS = "room players";
	public static final String COMPONENT_ROOM_OBJECTS = "room objs";
	public static final String COMPONENT_ROOM_EXITS = "room exits";
	public static final String COMPONENT_ROOM_DESCRIPTION = "room desc";
	
	public static enum GameMode {
		Game, CharacterManager
	};
	
	/**
	 * The server settings for this client
	 * @return
	 */
	public IStormFrontClientSettings getStormFrontClientSettings();
	
	/**
	 * @return The player ID of the current player
	 */
	public IProperty<String> getPlayerId();
	
	/**
	 * @return The roundtime property
	 */
	public IProperty<Integer> getRoundtime();
	
	/**
	 * Set up a roundtime to start with the next time sync.
	 * @param roundtimeEnd The end of the roundtime as sent from the server.
	 */
	public void setupRoundtime(Integer roundtimeEnd);
	
	/**
	 * Sync the current time as perceived by the server.
	 * @param now Time the server thinks it is.
	 */
	public void syncTime(Integer now);
	
	/**
	 * Wait out any active roundtimes.
	 */
	public void waitForRoundtime() throws InterruptedException;
	
	/**
	 * @return The health property
	 */
	public IProperty<BarStatus> getHealth();	
	
	/**
	 * @return The amount of mana in the mana bar.
	 */
	public IProperty<BarStatus> getMana();
	
	/**
	 * @return The amount of fatigue in the fatigue bar.
	 */
	public IProperty<BarStatus> getFatigue();
	
	/**
	 * @return The amount of spirit in the spirit bar.
	 */
	public IProperty<BarStatus> getSpirit();

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
	 * @return The current mounter count property
	 */
	public IProperty<Integer> getMonsterCount();
	
	/**
	 * @return The character status
	 */
	public ICharacterStatus getCharacterStatus();
		
	/**
	 * @return A list of currently running scripts
	 */
	public Collection<IScript> getRunningScripts();
	
	/**
	 * Add a script listener
	 * @param listener
	 */
	public void addScriptListener (IScriptListener listener);
	
	public void removeScriptListener (IScriptListener listener);
	
	/**
	 * @return The stormfront skin
	 */
	public IStormFrontSkin getStormFrontSkin();
	
	/**
	 * @return The stream for thoughts
	 */
	public IStream getThoughtsStream();
	
	/**
	 * @return The stream for deaths
	 */
	public IStream getDeathsStream();
	
	/**
	 * @return The stream for inventory
	 */
	public IStream getInventoryStream();
	
	/**
	 * @return The stream for room description/exits/etc.
	 */
	public IStream getRoomStream();
	
	/**
	 * @return The stream for familiars / wounds
	 */
	public IStream getFamiliarStream();
	
	/**
	 * @param componentName
	 * @return The component with the passed in name
	 */
	public IProperty<String> getComponent(String componentName);
	
	public void updateComponent(String name, WarlockString value);
	
	public void loadCmdlist();
	
	public String getCommand(String coord);
	
	public void runScript(String command);

}
