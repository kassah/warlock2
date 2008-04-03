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
package cc.warlock.core.script.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cc.warlock.core.client.IRoomListener;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IStreamListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.script.IMatch;
import cc.warlock.core.script.IScriptCommands;

public class ScriptCommands implements IScriptCommands, IStreamListener, IRoomListener
{

	protected IWarlockClient client;
	protected String scriptName;
	private boolean suspended = false;
	
	protected final Lock lock = new ReentrantLock();
	private final Condition gotResume = lock.newCondition();
	
	protected Collection<LinkedBlockingQueue<String>> textWaiters = Collections.synchronizedCollection(new ArrayList<LinkedBlockingQueue<String>>());
	
	protected Collection<IMatch> matches = new ArrayList<IMatch>();
	
	protected final Condition nextRoom = lock.newCondition();
	protected final Condition gotPromptCond = lock.newCondition();
	
	private List<Thread> scriptThreads = Collections.synchronizedList(new ArrayList<Thread>());
	
	public ScriptCommands (IWarlockClient client, String scriptName)
	{
		this.client = client;
		this.scriptName = scriptName;

		client.getDefaultStream().addStreamListener(this);
		client.addRoomListener(this);
	}
	
	public void echo (String text) {
		client.getDefaultStream().echo("[" + scriptName + "]: " + text + "\n");
	}
	
	public BlockingQueue<String> createLineQueue() {
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		synchronized(textWaiters) {
			if(!textWaiters.contains(queue)) {
				textWaiters.add(queue);
			}
		}
		return queue;
	}
	
	public IMatch matchWait (Collection<IMatch> matches, BlockingQueue<String> matchQueue, double timeout) throws InterruptedException {
		try {
			boolean ignoreTimeout = timeout <= 0.0;
			// run until we get a match or are told to stop
			matchWaitLoop: while(true) {
				String text = null;
				// wait for some text
				while(text == null) {
					text = matchQueue.poll(100L, TimeUnit.MILLISECONDS);
					// if we change the poll timeout, make sure the following line is updated
					if(!ignoreTimeout) {
						timeout -= 0.1;
						if(timeout <= 0)
							break matchWaitLoop;
					}
				}
				// try all of our matches
				for(IMatch match : matches) {
					if(match.matches(text)) {
						return match;
					}
				}
			}
		} finally {
			textWaiters.remove(matchQueue);
		}

		return null;
	}

	public void move (String direction) throws InterruptedException {
		put(direction);
		waitNextRoom();
	}

	public void waitNextRoom () throws InterruptedException {
		lock.lock();
		try {
			nextRoom.await();
			gotPromptCond.await();
		} finally {
			lock.unlock();
		}
	}

	public void pause (double seconds) throws InterruptedException {
		Thread.sleep((long)(seconds * 1000.0));
	}
	
	public void put (String text) throws InterruptedException {
		client.send("[" + scriptName + "]: ", text);
	}

	public void waitFor (IMatch match) throws InterruptedException {
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		String text = null;

		textWaiters.add(queue);
		try {
			while(true) {
				while(text == null) {
					text = queue.poll(100L, TimeUnit.MILLISECONDS);
				}
				if(match.matches(text)) {
					break;
				}
				text = null;
			}
		} finally {
			textWaiters.remove(queue);
		}
	}

	public void waitForPrompt () throws InterruptedException {
		lock.lock();
		try {
			gotPromptCond.await();
		} finally {
			lock.unlock();
		}
	}
	
	public IWarlockClient getClient() {
		return this.client;
	}
		
	public void streamCleared(IStream stream) {}
	public void streamEchoed(IStream stream, String text) {}
	public void streamFlush(IStream stream) {}
	
	public void streamPrompted(IStream stream, String prompt) {
		lock.lock();
		try {
			gotPromptCond.signalAll();
		} finally {
			lock.unlock();
		}
		receiveLine(prompt);
	}
	
	public void streamReceivedCommand (IStream stream, String text) {
		receiveText(text);
	}
	
	public void streamReceivedText(IStream stream, WarlockString text) {
		receiveText(text.toString());
	}
	
	public void componentUpdated(IStream stream, String id, String text) { }
	
	protected void receiveText(String text) {
		int end;
		while ((end = text.indexOf('\n')) != -1) {
			receiveLine(text.substring(0, end + 1));
			text = text.substring(end + 1);
		}
		if(text.length() != 0)
			receiveLine(text);
	}
	
	protected void receiveLine(String line) {
		synchronized(textWaiters) {
			for(LinkedBlockingQueue<String>  queue : textWaiters) {
				try {
					queue.put(line);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void nextRoom() {
		lock.lock();
		try {
			nextRoom.signalAll();
		} finally {
			lock.unlock();
		}
	}

	
	public void stop() {
		interrupt();

		client.getDefaultStream().removeStreamListener(this);
		client.removeRoomListener(this);
	}
	
	public void interrupt() {
		lock.lock();
		try {
			synchronized(scriptThreads) {
				for(Thread scriptThread : scriptThreads)
					scriptThread.interrupt();
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void resume() {
		this.suspended = false;
		
		lock.lock();
		try {
			gotResume.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public void suspend() {
		this.suspended = true;
	}
	
	public boolean isSuspended() {
		return suspended;
	}
	
	public void waitForResume() throws InterruptedException {
		while(suspended) {
			lock.lock();
			try {
				gotResume.await();
			} finally {
				lock.unlock();
			}
		}
	}
	
	public void addThread(Thread thread) {
		scriptThreads.add(thread);
	}
	
	public void removeThread(Thread thread) {
		scriptThreads.remove(thread);
	}
}
