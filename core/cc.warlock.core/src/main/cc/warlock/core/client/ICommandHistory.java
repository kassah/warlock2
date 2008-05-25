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
 * Created on Jan 15, 2005
 */
package cc.warlock.core.client;

/**
 * @author Marshall
 * 
 * The warlock client's command history
 */
public interface ICommandHistory {
	
	/**
	 * @return The last command entered in this command history
	 */
	public ICommand getLastCommand();
	
	/**
	 * @return The previous command in the command history towards the past
	 */
	public ICommand prev();
	
	/**
	 * @return The next command in the command history towards the present
	 */
	public ICommand next();
	
	/**
	 * @param text
	 * @return The command in the history from the current position that matches the text
	 */
	public ICommand search(String text);

	/**
	 * @param text
	 * @return The command in the history from before the current position that matches the text
	 */
	public ICommand searchBefore(String text);
	
	/**
	 * @return The current command being edited.
	 */
	public ICommand current();
	
	/**
	 * @return The size of the command history
	 */
	public int size();
	
	/**
	 * @param position
	 * @return The command at the given position
	 */
	public ICommand getCommandAt(int position);
	
	/**
	 * Reset to the position of the "latest" command
	 */
	public void resetPosition();
	
	/**
	 * Add a command to the command history
	 * @param command
	 */
	public void addCommand (ICommand command);
	
	/**
	 * Save this command history
	 */
	public void save ();
	
	/**
	 * Add a command history listener
	 * @param listener
	 */
	public void addCommandHistoryListener (ICommandHistoryListener listener);
	
	/**
	 * Remove a command history listener
	 * @param listener
	 */
	public void removeCommandHistoryListener (ICommandHistoryListener listener);
}
