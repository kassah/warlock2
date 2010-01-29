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
import cc.warlock.core.client.IProperty;
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
	
	protected IProperty<String> streamName, streamTitle;
	private ArrayList<IStreamListener> listeners = new ArrayList<IStreamListener>();
	protected boolean isPrompting = false;
	private boolean hasView = false;
	protected boolean isLogging = false;
	
	protected Stream (IWarlockClient client, String streamName) {
		this.client = client;
		this.streamName = new Property<String>("streamName", null);
		this.streamName.set(streamName);
		this.streamTitle = new Property<String>("streamTitle", null);
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
	
	public void send(String text) {
		send(new WarlockString(text));
	}
	
	public synchronized void send(WarlockString text) {
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
	
	public IProperty<String> getName() {
		return streamName;
	}
	
	public IWarlockClient getClient ()
	{
		return client;
	}

	public IProperty<String> getTitle() {
		return streamTitle;
	}
	
	public boolean hasView() {
		return hasView;
	}
	
	public void setView(boolean view) {
		hasView = view;
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
}
