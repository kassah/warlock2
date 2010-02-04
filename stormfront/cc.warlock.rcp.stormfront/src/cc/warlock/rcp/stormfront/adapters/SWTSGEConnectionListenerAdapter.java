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
import cc.warlock.rcp.ui.client.CatchingRunnable;

/**
 * @author Marshall
 */
public class SWTSGEConnectionListenerAdapter implements ISGEConnectionListener {

	private ISGEConnectionListener listener;
	
	public SWTSGEConnectionListenerAdapter (ISGEConnectionListener listener)
	{
		this.listener = listener;
	}
	
	protected void run(Runnable runnable) {
		Display.getDefault().asyncExec(new CatchingRunnable(runnable));
	}
	
	private class LoginReadyRunnable implements Runnable {
		private SGEConnection connection;
		
		public LoginReadyRunnable(SGEConnection connection) {
			this.connection = connection;
		}
		
		public void run () {
			listener.loginReady(connection);
		}
	}
	
	public void loginReady(SGEConnection connection) {
		run(new LoginReadyRunnable(connection));
	}

	private class LoginFinishedRunnable implements Runnable {
		private SGEConnection connection;
		
		public LoginFinishedRunnable(SGEConnection connection) {
			this.connection = connection;
		}
		
		public void run () {
			listener.loginFinished(connection);
		}
	}
	
	public void loginFinished(SGEConnection connection) {
		run(new LoginFinishedRunnable(connection));
	}

	private class GamesReadyRunnable implements Runnable {
		private SGEConnection connection;
		private List<? extends ISGEGame> games;
		
		public GamesReadyRunnable(SGEConnection connection, List<? extends ISGEGame> games) {
			this.connection = connection;
			this.games = games;
		}
		
		public void run () {
			listener.gamesReady(connection, games);
		}
	}
	
	public void gamesReady(SGEConnection connection, List<? extends ISGEGame> games) {
		run(new GamesReadyRunnable(connection, games));
	}

	private class CharactersReadyRunnable implements Runnable {
		private SGEConnection connection;
		private Map<String, String> characters;
		
		public CharactersReadyRunnable(SGEConnection connection, Map<String, String> characters) {
			this.connection = connection;
			this.characters = characters;
		}
		
		public void run () {
			listener.charactersReady(connection, characters);
		}
	}
	
	public void charactersReady(SGEConnection connection, Map<String, String> characters) {
		run(new CharactersReadyRunnable(connection, characters));
	}

	private class ReadyToPlayRunnable implements Runnable {
		private SGEConnection connection;
		private Map<String,String> loginProperties;
		
		public ReadyToPlayRunnable(SGEConnection connection, Map<String, String> loginProperties) {
			this.connection = connection;
			this.loginProperties = loginProperties;
		}
		
		public void run () {
			listener.readyToPlay(connection, loginProperties);
		}
	}
	
	public void readyToPlay(SGEConnection connection, Map<String,String> loginProperties) {
		run(new ReadyToPlayRunnable(connection, loginProperties));
	}
	
	private class SgeErrorRunnable implements Runnable {
		private SGEConnection connection;
		private int errorCode;
		
		public SgeErrorRunnable(SGEConnection connection, int errorCode) {
			this.connection = connection;
			this.errorCode = errorCode;
		}
		
		public void run () {
			listener.sgeError(connection, errorCode);
		}
	}
	
	public void sgeError(SGEConnection connection, int errorCode) {
		run(new SgeErrorRunnable(connection, errorCode));
	}
}
