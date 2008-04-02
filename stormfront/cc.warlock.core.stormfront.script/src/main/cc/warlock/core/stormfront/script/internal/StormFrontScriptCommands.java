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
package cc.warlock.core.stormfront.script.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.IStream;
import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.internal.ScriptCommands;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.script.IStormFrontScriptCommands;

public class StormFrontScriptCommands extends ScriptCommands implements IStormFrontScriptCommands, IPropertyListener<Integer> {

	protected IStormFrontClient sfClient;
	protected IScript script;
	private int typeAhead = 0;
	
	public StormFrontScriptCommands (IStormFrontClient client, String name)
	{
		super(client, name);
		this.sfClient = client;
		waitingForRoundtime = false;
		
		client.getRoundtime().addListener(this);
		client.getDeathsStream().addStreamListener(this);
		client.getFamiliarStream().addStreamListener(this);
		client.addRoomListener(this);
	}
	
	public StormFrontScriptCommands (IStormFrontClient client, IScript script)
	{
		this(client, script.getName());
		
		this.script = script;
	}
	
	public IStormFrontClient getStormFrontClient() {
		return sfClient;
	}
	
	@Override
	public void put(String text) {
		if(typeAhead >= 2)
			this.waitForPrompt();
		typeAhead++;
		super.put(text);
	}
	
	@Override
	public void streamPrompted(IStream stream, String prompt) {
		if(typeAhead > 0)
			typeAhead--;
		super.streamPrompted(stream, prompt);
	}
	
	protected boolean waitingForRoundtime;
	public void waitForRoundtime ()
	{
		if(!interrupted) {
			try {
				while(sfClient.getRoundtime().get() > 0 && !interrupted) {
					super.pause(sfClient.getRoundtime().get() + 1);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void propertyActivated(IProperty<Integer> property) {}
	public void propertyChanged(IProperty<Integer> property, Integer oldValue) {
		if (property.getName().equals("roundtime"))
		{
			if (property.get() == 0) waitingForRoundtime = false;
		}
	}
	
	public void propertyCleared(IProperty<Integer> property, Integer oldValue) {}
	
	private Map<IMatch, Runnable> actions = Collections.synchronizedMap(new HashMap<IMatch, Runnable>());
	
	protected  class ScriptActionThread implements Runnable {
		public void run() {
			LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
			synchronized(textWaiters) {
				textWaiters.add(queue);
			}
			actionLoop: while(true) {
				String text = null;

				while(text == null) {
					try {
						text = queue.poll(100L, TimeUnit.MILLISECONDS);
						if(actions.size() == 0 || interrupted) {
							break actionLoop;
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				synchronized(actions) {
					for(Map.Entry<IMatch, Runnable> action : actions.entrySet()) {
						if(action.getKey().matches(text))
							action.getValue().run();
					}
				}
			}
			synchronized(textWaiters) {
				textWaiters.remove(queue);
			}
		}
	}
	
	public void addAction(Runnable action, IMatch match) {
		if(actions.size() == 0) {
			new Thread(new ScriptActionThread()).start();
		}
		
		synchronized(actions) {
			actions.put(match, action);
		}
	}
	
	public void clearActions() {
		actions.clear();
	}
	
	public void removeAction(IMatch action) {
		synchronized(actions) {
			actions.remove(action);
		}
	}
	
	public void removeAction(String text) {
		synchronized(actions) {
			for(IMatch match : actions.keySet()) {
				// remove the element with the same name as text
				if(match.getText().equals(text)) {
					actions.remove(match);
				}
			}
		}
	}
	
	@Override
	public void stop() {
		super.stop();
		clearActions();
	}
}
