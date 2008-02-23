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
	protected boolean roomWaiting = false;
	
	protected final Condition gotPromptCond = lock.newCondition();
	protected boolean gotPrompt = false;
	
	protected boolean interrupted = false;
	
	private List<Thread> pausedThreads = Collections.synchronizedList(new ArrayList<Thread>());
	
	public ScriptCommands (IWarlockClient client, String scriptName)
	{
		this.client = client;
		this.scriptName = scriptName;
		this.gotPrompt = client.getDefaultStream().isPrompting();

		client.getDefaultStream().addStreamListener(this);
		client.addRoomListener(this);
	}
	
	public void echo (String text) {
		client.getDefaultStream().echo("[" + scriptName + "]: " + text + "\n");
	}
	
	protected void assertPrompt() {
		if (!gotPrompt) {
			lock.lock();
			try {
				while(!interrupted && !gotPrompt)
					gotPromptCond.await();
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
	
	public BlockingQueue<String> getLineQueue() {
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		synchronized(textWaiters) {
			if(!textWaiters.contains(queue)) {
				textWaiters.add(queue);
			}
		}
		return queue;
	}
	
	public IMatch matchWait (Collection<IMatch> matches, BlockingQueue<String> matchQueue, double timeout) {
		try {
			boolean ignoreTimeout = timeout <= 0.0;
			// run until we get a match or are told to stop
			matchWaitLoop: while(true) {
				String text = null;
				// wait for some text
				while(text == null) {
					try {
						text = matchQueue.poll(100L, TimeUnit.MILLISECONDS);
						// if we change the poll timeout, make sure the following line is updated
						if(!ignoreTimeout) {
							timeout -= 0.1;
							if(timeout <= 0)
								break matchWaitLoop;
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					if(interrupted)
						break matchWaitLoop;
				}
				// try all of our matches
				for(IMatch match : matches) {
					if(match.matches(text)) {
						return match;
					}
				}
			}
		} finally {
			synchronized(textWaiters) {
				textWaiters.remove(matchQueue);
			}
		}

		return null;
	}

	public void move (String direction) {
		put(direction);
		waitNextRoom();
	}

	public void waitNextRoom () {
		lock.lock();
		try {
			roomWaiting = true;
			while (!interrupted && roomWaiting) {
				nextRoom.await();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			roomWaiting = false;
			lock.unlock();
		}
		assertPrompt();
	}

	public void pause (double seconds) {
		Thread thread = Thread.currentThread();
		try {
			synchronized(pausedThreads) {
				pausedThreads.add(thread);
			}
			Thread.sleep((long)(seconds * 1000.0));
		} catch(InterruptedException e) {
			// we really don't care
		} finally {
			synchronized(pausedThreads) {
				pausedThreads.remove(thread);
			}
		}
	}
	
	public void put (String text) {
		assertPrompt();
		
		client.send("[" + scriptName + "]: ", text);
	}

	public void waitFor (IMatch match) {
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		String text = null;

		synchronized(textWaiters) {
			textWaiters.add(queue);
		}
		try {
			waitForLoop: while(true) {
				while(text == null) {
					try {
						text = queue.poll(100L, TimeUnit.MILLISECONDS);
					} catch(Exception e) {
						e.printStackTrace();
					}
					if(interrupted)
						break waitForLoop;
				}
				if(match.matches(text)) {
					break;
				}
				text = null;
			}
		} finally {
			synchronized(textWaiters) {
				textWaiters.remove(queue);
			}
		}
	}

	public void waitForPrompt () {
		lock.lock();
		try {
			gotPromptCond.await();
		} catch(Exception e) {
			e.printStackTrace();
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
		gotPrompt = true;
		lock.lock();
		try {
			gotPromptCond.signalAll();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void streamReceivedCommand (IStream stream, String text) {
		gotPrompt = false;
		receiveText(text);
	}
	
	public void streamReceivedText(IStream stream, WarlockString text) {
		gotPrompt = false;
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
			// TODO we should probably set gotPrompt to false whenever we get
			// a tag. This is just to fix the case for moving between rooms.
			gotPrompt = false;
			roomWaiting = false;
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
			interrupted = true;
			gotPromptCond.signalAll();
			nextRoom.signalAll();
			gotResume.signalAll();
			synchronized(pausedThreads) {
				for(Thread pausedThread : pausedThreads)
					pausedThread.interrupt();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void clearInterrupt() {
		interrupted = false;
	}
	
	public void resume() {
		this.suspended = false;
		
		lock.lock();
		try {
			gotResume.signalAll();
		} catch(Exception e) {
			e.printStackTrace();
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
	
	public void waitForResume() {
		while(suspended) {
			lock.lock();
			try {
				gotResume.await();
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
}
