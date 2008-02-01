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
 * Created on Jan 13, 2005
 */
package cc.warlock.rcp.stormfront.adapters;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.stormfront.network.ISGEConnectionListener;
import cc.warlock.core.stormfront.network.ISGEGame;
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
		public List<? extends ISGEGame> games;
		
		public void run () {
			listener.gamesReady(connection, games);
		}
	}
	
	public void gamesReady(SGEConnection connection, List<? extends ISGEGame> games) {
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
