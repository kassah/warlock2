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
 * Created on Sep 20, 2004
 */
package cc.warlock.core.stormfront.network;

import java.util.List;
import java.util.Map;

/**
 * @author Marshall
 */
public interface ISGEConnectionListener {

	/**
	 * This method will be called when the SGEConnection is ready to be logged in.
	 */
	public void loginReady(SGEConnection connection);
	
	/**
	 * This method will be called when the call to SGEConnection.login() has succesfully finished.
	 */
	public void loginFinished (SGEConnection connection);
	
	/**
	 * This method will be called when the SGE server returns with a list of playable games (a Map of gamecode => description)
	 */
	public void gamesReady(SGEConnection connection, List<? extends ISGEGame> games);

	/**
	 * This method will be called when the SGE server returns a list of characters for the selected game (a Map of character code => character name)
	 * @param connection
	 * @param characters
	 */
	public void charactersReady(SGEConnection connection, Map<String, String> characters);
	
	/**
	 * This method will be called when the SGE server returns a list of login properties after SGE sends the character to play with.
	 * @param connection
	 * @param loginProperties
	 */
	public void readyToPlay(SGEConnection connection, Map<String,String> loginProperties);
	
	/**
	 * An error occurred during the login process.
	 * @param connection The connection the error occurred on
	 * @param errorCode will be one of:
	 * 			SGEConnection.INVALID_PASSWORD
	 * 			SGEConnection.INVALID_ACCOUNT
	 * 			SGEConnection.ACCOUNT_BANNED
	 * 			SGEConnection.ACCOUNT_EXPIRED
	 */
	public void sgeError (SGEConnection connection, int errorCode);
}
