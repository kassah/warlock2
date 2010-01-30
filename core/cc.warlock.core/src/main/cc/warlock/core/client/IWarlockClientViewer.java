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

import java.io.InputStream;



/**
 * @author Marshall
 * 
 * A Warlock Client will have 0-* IWarlockClientViewers.
 * The implementor of this class will be responsible for echoing
 * and appending text to the view of this client.  
 */
public interface IWarlockClientViewer {

	public IWarlockClient getWarlockClient ();
	
	public String getCurrentCommand ();
	
	public void setCurrentCommand (String command);
	
	public void append(char c);
	
	public void submit();
	
	public void prevCommand();
	
	public void nextCommand();
	
	public void searchHistory();
	
	public void repeatLastCommand();
	
	public void repeatSecondToLastCommand();
	
	public void copy();
	
	public void paste();
	
	public void playSound(InputStream soundStream);
}
