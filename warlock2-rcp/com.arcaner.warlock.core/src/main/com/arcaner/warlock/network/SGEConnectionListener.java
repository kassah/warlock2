/*
 * Created on Jan 1, 2005
 */
package com.arcaner.warlock.network;

import java.util.Map;

/**
 * @author Marshall
 *
 * A stub implementation class so implementors can implement only methods they want.
 */
public class SGEConnectionListener implements ISGEConnectionListener {

	/* (non-Javadoc)
	 * @see com.arcaner.warlock.network.ISGEConnectionListener#loginReady(com.arcaner.warlock.network.SGEConnection)
	 */
	public void loginReady(SGEConnection connection) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.arcaner.warlock.network.ISGEConnectionListener#loginFinished(com.arcaner.warlock.network.SGEConnection, int)
	 */
	public void loginFinished(SGEConnection connection, int status) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.arcaner.warlock.network.ISGEConnectionListener#gamesReady(com.arcaner.warlock.network.SGEConnection, java.util.Map)
	 */
	public void gamesReady(SGEConnection connection, Map<String,String> games) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.arcaner.warlock.network.ISGEConnectionListener#charactersReady(com.arcaner.warlock.network.SGEConnection, java.util.Map)
	 */
	public void charactersReady(SGEConnection connection, Map<String,String> characters) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.arcaner.warlock.network.ISGEConnectionListener#readyToPlay(com.arcaner.warlock.network.SGEConnection, java.util.Map)
	 */
	public void readyToPlay(SGEConnection connection, Map<String,String> loginProperties) {
		// TODO Auto-generated method stub

	}

}
