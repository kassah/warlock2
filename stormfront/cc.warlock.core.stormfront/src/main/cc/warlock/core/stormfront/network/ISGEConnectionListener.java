/*
 * Created on Sep 20, 2004
 */
package cc.warlock.core.stormfront.network;

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
	public void gamesReady(SGEConnection connection, Map<String, String> games);

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
