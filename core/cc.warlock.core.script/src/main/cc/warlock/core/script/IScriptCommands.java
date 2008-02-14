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
package cc.warlock.core.script;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IWarlockClient;

public interface IScriptCommands extends IRoomListener {

	public void put (String text);
	
	public void echo (String text);
	
	public void move (String direction);
	
	public void waitFor (IMatch match);
	
	public void waitNextRoom ();
	
	public BlockingQueue<String> getLineQueue();
	
	public IMatch matchWait (Collection<IMatch> matches, BlockingQueue<String> matchQueue, double timeout);
	
	public void pause (double seconds);
	
	public void waitForPrompt ();
	
	public IWarlockClient getClient();
	
	public void stop();
	
	public void interrupt();
	public void clearInterrupt();
	
	public void suspend();
	public void resume();
	public boolean isSuspended();
	public void waitForResume();
}
