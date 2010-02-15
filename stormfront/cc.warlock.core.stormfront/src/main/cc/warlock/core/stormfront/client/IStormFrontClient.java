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
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptListener;

/**
 * @author Marshall
 */
public interface IStormFrontClient extends IWarlockClient, IRoomListener {
	
	/**
	 * @return The player ID of the current player
	 */
	public String getPlayerId();
	
	public void setPlayerId(String playerId);
	
	/**
	 * @return The gameCode of the current player
	 */
	public String getGameCode();
	
	/**
	 * @return The roundtime property
	 */
	public IProperty<Integer> getRoundtime();
	
	/**
	 * @return The casttime property
	 */
	public IProperty<Integer> getCasttime();
	
	/**
	 * Set up a roundtime to start with the next time sync.
	 * @param roundtimeEnd The end of the roundtime as sent from the server.
	 */
	public void setupRoundtime(Long roundtimeEnd);
	
	/**
	 * Set up a casttime to start with the next time sync.
	 * @param casttimeEnd The end of the casttime as sent from the server.
	 */
	public void setupCasttime(Long casttimeEnd);
	
	/**
	 * Sync the current time as perceived by the server.
	 * @param now Time the server thinks it is.
	 */
	public void syncTime(Long now);
	
	/**
	 * Wait out any active roundtimes.
	 */
	public void waitForRoundtime(double delay) throws InterruptedException;
	
	/**
	 * Wait out any active casttimes.
	 */
	public void waitForCasttime(double delay) throws InterruptedException;
	
	public int getRoundtimeLength();
	
	public int getCasttimeLength();
	
	/**
	 * @return The vital associated with id
	 */
	public String getVital(String id);
	
	public String setVital(String id, String value);

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
	 * @return The associated SFDialog
	 */
	public IProperty<IStormFrontDialogMessage> getDialog(String id);
	
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
	 * @param componentName
	 * @return The component with the passed in name
	 */
	public IProperty<String> getComponent(String componentName);
	
	public void updateComponent(String name, WarlockString value);
	
	public void loadCmdlist();
	
	public String getCommand(String coord);
	
	public void runScript(String command);
	
	public void launchURL(String url);
	
	public void appendImage(String pictureId);
	
	public void startedDownloadingServerSettings();
	
	public void finishedDownloadingServerSettings(String str);
	
	public void receivedServerSetting(String setting);

	public IWarlockStyle getNamedStyle(String name);
}
