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
 * A stream is StormFront's idea of a text buffer for a window. This is the Interface representing that buffer.
 */
public interface IStream {
	
	public void clear();
	
	public IProperty<String> getName();
	
	public IProperty<String> getTitle();
	
	public void send (String text);
	public void send (WarlockString text);
	
	public void prompt(String prompt);
	public void sendCommand(ICommand command);
	public boolean isPrompting();
	public void flush();
	public boolean hasView();
	public void setView(boolean view);
	
	public void echo(String text);
	
	public void addStreamListener(IStreamListener listener);
	public void removeStreamListener(IStreamListener listener);
	
	public IWarlockClient getClient();
	
	public void updateComponent(String id, WarlockString text);
}
