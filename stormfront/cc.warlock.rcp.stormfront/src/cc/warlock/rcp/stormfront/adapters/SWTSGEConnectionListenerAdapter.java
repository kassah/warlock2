/*
 * Created on Jan 13, 2005
 */
package cc.warlock.rcp.stormfront.adapters;

import java.util.Map;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.stormfront.network.ISGEConnectionListener;
import cc.warlock.core.stormfront.network.SGEConnection;

/**
 * @author Marshall
 */
public class SWTSGEConnectionListenerAdapter implements ISGEConnectionListener {

	private ISGEConnectionListener listener;
	private LoginReadyRunnable loginReadyRunnable;
	private LoginFinishedRunnable loginFinishedRunnable;
	private GamesReadyRunnable gamesReadyRunnable;
	private CharactersReadyRunnable charactersReadyRunnable;
	private ReadyToPlayRunnable readyToPlayRunnable;
	private SgeErrorRunnable sgeErrorRunnable;
	
	public SWTSGEConnectionListenerAdapter (ISGEConnectionListener listener)
	{
		this.listener = listener;

		loginReadyRunnable = new LoginReadyRunnable();
		loginFinishedRunnable = new LoginFinishedRunnable();
		gamesReadyRunnable = new GamesReadyRunnable();
		charactersReadyRunnable = new CharactersReadyRunnable();
		readyToPlayRunnable = new ReadyToPlayRunnable();
		sgeErrorRunnable = new SgeErrorRunnable();
	}
	
	private class LoginReadyRunnable implements Runnable {
		public SGEConnection connection;
		
		public void run () {
			listener.loginReady(connection);
		}
	}
	
	public void loginReady(SGEConnection connection) {
		loginReadyRunnable.connection = connection;
		Display.getDefault().asyncExec(loginReadyRunnable);
	}

	private class LoginFinishedRunnable implements Runnable {
		public SGEConnection connection;
		
		public void run () {
			listener.loginFinished(connection);
		}
	}
	
	public void loginFinished(SGEConnection connection) {
		loginFinishedRunnable.connection = connection;
		Display.getDefault().asyncExec(loginFinishedRunnable);
	}

	private class GamesReadyRunnable implements Runnable {
		public SGEConnection connection;
		public Map<String, String> games;
		
		public void run () {
			listener.gamesReady(connection, games);
		}
	}
	
	public void gamesReady(SGEConnection connection, Map<String, String> games) {
		gamesReadyRunnable.connection = connection;
		gamesReadyRunnable.games = games;
		Display.getDefault().asyncExec(gamesReadyRunnable);
	}

	private class CharactersReadyRunnable implements Runnable {
		public SGEConnection connection;
		public Map<String, String> characters;
		
		public void run () {
			listener.charactersReady(connection, characters);
		}
	}
	
	public void charactersReady(SGEConnection connection, Map<String, String> characters) {
		charactersReadyRunnable.connection = connection;
		charactersReadyRunnable.characters = characters;
		Display.getDefault().asyncExec(charactersReadyRunnable);
	}

	private class ReadyToPlayRunnable implements Runnable {
		public SGEConnection connection;
		public Map<String,String> loginProperties;
		
		public void run () {
			listener.readyToPlay(connection, loginProperties);
		}
	}
	
	public void readyToPlay(SGEConnection connection, Map<String,String> loginProperties) {
		readyToPlayRunnable.connection = connection;
		readyToPlayRunnable.loginProperties = loginProperties;
		Display.getDefault().asyncExec(readyToPlayRunnable);
	}
	
	private class SgeErrorRunnable implements Runnable {
		public SGEConnection connection;
		public int errorCode;
		
		public void run () {
			listener.sgeError(connection, errorCode);
		}
	}
	
	public void sgeError(SGEConnection connection, int errorCode) {
		sgeErrorRunnable.connection = connection;
		sgeErrorRunnable.errorCode = errorCode;
		Display.getDefault().asyncExec(sgeErrorRunnable);
	}

}
