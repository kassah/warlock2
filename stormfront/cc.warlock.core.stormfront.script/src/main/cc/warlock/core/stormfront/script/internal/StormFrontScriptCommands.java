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

import cc.warlock.core.client.IStream;
import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.internal.ScriptCommands;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.script.IStormFrontScriptCommands;

public class StormFrontScriptCommands extends ScriptCommands implements IStormFrontScriptCommands {

	protected IStormFrontClient sfClient;
	protected IScript script;
	private int typeAhead = 0;
	
	public StormFrontScriptCommands (IStormFrontClient client, String name)
	{
		super(client, name);
		this.sfClient = client;
		
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
	
	public void waitForRoundtime ()
	{
		try {
			while(sfClient.getRoundtime().get() > 0 && script.isRunning())
				Thread.sleep((sfClient.getRoundtime().get() + 1) * 1000L);
		} catch(InterruptedException e) {
			// we really don't care
		}
	}
	
	private Map<IMatch, Runnable> actions = Collections.synchronizedMap(new HashMap<IMatch, Runnable>());
	
	protected  class ScriptActionThread implements Runnable {
		public void run() {
			StormFrontScriptCommands.this.addThread(Thread.currentThread());
			LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
			textWaiters.add(queue);
			try {
				actionLoop: while(script.isRunning()) {
					String text = null;

					while(text == null) {
						text = queue.poll(100L, TimeUnit.MILLISECONDS);
						if(actions.size() == 0) {
							break actionLoop;
						}
					}
					synchronized(actions) {
						for(Map.Entry<IMatch, Runnable> action : actions.entrySet()) {
							if(action.getKey().matches(text))
								action.getValue().run();
						}
					}
				}
			} catch(InterruptedException e) {
				// nothing to do
			} finally {
				textWaiters.remove(queue);
				StormFrontScriptCommands.this.removeThread(Thread.currentThread());
			}
		}
	}
	
	public void addAction(Runnable action, IMatch match) {
		synchronized(actions) {
			if(actions.size() == 0) {
				actions.put(match, action);
				new Thread(new ScriptActionThread()).start();
			} else {
				actions.put(match, action);
			}
		}
	}
	
	public void clearActions() {
		actions.clear();
	}
	
	public void removeAction(IMatch action) {
		actions.remove(action);
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
