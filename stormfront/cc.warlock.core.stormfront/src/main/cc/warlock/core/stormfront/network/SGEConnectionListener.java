/*
 * Created on Jan 1, 2005
 */
package cc.warlock.core.stormfront.network;

import java.util.Map;

/**
 * @author Marshall
 *
 * A stub implementation class so implementors can implement only methods they want.
 */
public class SGEConnectionListener implements ISGEConnectionListener {

	/* (non-Javadoc)
	 * @see cc.warlock.network.ISGEConnectionListener#loginReady(cc.warlock.network.SGEConnection)
	 */
	public void loginReady(SGEConnection connection) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see cc.warlock.network.ISGEConnectionListener#loginFinished(cc.warlock.network.SGEConnection, int)
	 */
	public void loginFinished(SGEConnection connection, int status) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see cc.warlock.network.ISGEConnectionListener#gamesReady(cc.warlock.network.SGEConnection, java.util.Map)
	 */
	public void gamesReady(SGEConnection connection, Map<String,String> games) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see cc.warlock.network.ISGEConnectionListener#charactersReady(cc.warlock.network.SGEConnection, java.util.Map)
	 */
	public void charactersReady(SGEConnection connection, Map<String,String> characters) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see cc.warlock.network.ISGEConnectionListener#readyToPlay(cc.warlock.network.SGEConnection, java.util.Map)
	 */
	public void readyToPlay(SGEConnection connection, Map<String,String> loginProperties) {
		// TODO Auto-generated method stub

	}

}
