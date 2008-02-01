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
 * Created on Jan 1, 2005
 */
package cc.warlock.core.stormfront.network;

import java.util.List;
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
	public void loginFinished(SGEConnection connection) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see cc.warlock.network.ISGEConnectionListener#gamesReady(cc.warlock.network.SGEConnection, java.util.Map)
	 */
	public void gamesReady(SGEConnection connection, List<? extends ISGEGame> games) {
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

	public void sgeError(SGEConnection connection, int errorCode) {
		// TODO Auto-generated method stub
		
	}
}
