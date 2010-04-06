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
 * Created on Jan 16, 2005
 */
package cc.warlock.core.client;


/**
 * @author Marshall
 *
 * IStreamListener implementations will subscribe to an IStream and
 *  receive an event when the Stream receives new data.
 */
public interface IStreamListener {
	/**
	 * Notify Listner than a new stream has been created.
	 * 
	 * @param stream New stream
	 */
	public void streamCreated(IStream stream);
	
	/**
	 * Notify when stream recieves text
	 * 
	 * @param stream Stream Text Belongs to
	 * @param text Text inserted into stream
	 */
	public void streamReceivedText (IStream stream, WarlockString text);
	
	/**
	 * Stream has received a prompt.
	 * 
	 * @param stream
	 * @param prompt
	 */
	public void streamPrompted (IStream stream, String prompt);
	
	/**
	 * Stream has had a command pushed through it.
	 * 
	 * @param stream
	 * @param command
	 */
	public void streamReceivedCommand (IStream stream, ICommand command);
	
	/**
	 * Stream has been asked to clear. i.e. an updated copy of the streams content is coming through.
	 * 
	 * @param stream
	 */
	public void streamCleared (IStream stream);
	
	/**
	 * Flush Stream?
	 * TODO: What is this?
	 * 
	 * @param stream
	 */
	public void streamFlush (IStream stream);
	
	
	/**
	 * Stream title has changed.
	 * 
	 * @param stream
	 * @param title
	 */
	public void streamTitleChanged(IStream stream, String title);
	
	/**
	 * A component in the stream has been updated. A component is a small bit of text within the stream.
	 * 
	 * @param stream
	 * @param id
	 * @param value
	 */
	public void componentUpdated(IStream stream, String id, WarlockString value);
}
