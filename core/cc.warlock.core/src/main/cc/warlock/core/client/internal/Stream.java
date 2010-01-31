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
package cc.warlock.core.client.internal;

import java.util.ArrayList;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockString;


/**
 * @author Marshall
 * 
 * The internal implementation of a StormFront stream.
 */
public class Stream implements IStream {
	
	protected IWarlockClient client;
	
	protected String title;
	protected String subtitle;
	private ArrayList<IStreamListener> listeners = new ArrayList<IStreamListener>();
	protected boolean isPrompting = false;
	private boolean isClosed = true;
	private String closedStyle;
	private String closedTarget = "main";
	private String streamName;
	protected boolean isLogging = false;
	private String location;
	
	protected Stream (IWarlockClient client, String streamName) {
		this.client = client;
		this.streamName = streamName;
	}

	public synchronized void addStreamListener(IStreamListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}
	
	public synchronized void removeStreamListener(IStreamListener listener) {
		listeners.remove(listener);
	}

	public synchronized void clear() {
		for(IStreamListener listener : listeners) {
			listener.streamCleared(this);
		}
	}
	
	public synchronized void flush() {
		for(IStreamListener listener : listeners) {
			listener.streamFlush(this);
		}
	}
	
	public synchronized void put(WarlockString text) {
		if (isLogging && client.getLogger() != null) {
			client.getLogger().logText(text);
		}

		for(IStreamListener listener : listeners) {
			try {
				listener.streamReceivedText(this, text);
			} catch (Throwable t) {
				// TODO Auto-generated catch block
				t.printStackTrace();
			}
		}
		
		if(isClosed && !closedTarget.equals("") && !streamName.equals(closedTarget)) {
			WarlockString closedText = new WarlockString(text.toString(), new WarlockStyle(closedStyle));
			client.getStream(closedTarget).put(closedText);
		}
		
		isPrompting = false;
	}
	
	public synchronized void prompt(String prompt) {
		isPrompting = true;
		
		if (isLogging && client.getLogger() != null) {
			client.getLogger().logPrompt(prompt);
		}

		for (IStreamListener listener : listeners)
		{
			try {
				listener.streamPrompted(this, prompt);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	public synchronized void sendCommand(ICommand command) {
		if (isLogging && client.getLogger() != null) {
			client.getLogger().logEcho(command.getText());
		}

		for (IStreamListener listener : listeners)
		{
			listener.streamReceivedCommand(this, command);
		}
		
		isPrompting = false;
	}
	
	public boolean isPrompting () {
		return isPrompting;
	}
	
	public synchronized void echo(String text) {
		if (isLogging && client.getLogger() != null) {
			client.getLogger().logEcho(text);
		}

		for (IStreamListener listener : listeners)
		{
			try {
				listener.streamEchoed(this, text);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	public String getName() {
		return streamName;
	}
	
	public IWarlockClient getClient ()
	{
		return client;
	}

	public void setTitle(String title) {
		this.title = title;
		
		for(IStreamListener listener : listeners) {
			listener.streamTitleChanged(this, title);
		}
	}
	
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getFullTitle() {
		return subtitle == null ? title : title + subtitle;
	}
	
	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}
	
	public void setClosedTarget(String target) {
		this.closedTarget = target;
	}
	
	public void setClosedStyle(String style) {
		this.closedStyle = style;
	}
	
	public synchronized void updateComponent(String id, WarlockString text) {
		for (IStreamListener listener : listeners)
		{
			try {
				listener.componentUpdated(this, id, text);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	public void setLogging (boolean logging) {
		this.isLogging = logging;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return location;
	}
}
