package cc.warlock.core.script.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.script.Match;

public class ScriptCommands implements IScriptCommands, IStreamListener, IRoomListener
{

	protected IWarlockClient client;
	protected String scriptName;
	
	protected final Lock lock = new ReentrantLock();
	
	protected List<LinkedBlockingQueue<String>> textWaiters = Collections.synchronizedList(new ArrayList<LinkedBlockingQueue<String>>());
	
	protected List<Match> matches = new ArrayList<Match>();
	protected LinkedBlockingQueue<String> matchQueue = new LinkedBlockingQueue<String>();
	
	protected final Condition nextRoom = lock.newCondition();
	protected boolean roomWaiting = false;
	
	protected final Condition gotPromptCond = lock.newCondition();
	protected int promptWaiters = 0;
	protected boolean gotPrompt = false;
	
	protected boolean interrupted = false;
	
	protected Thread pausedThread;
	
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
		if (!gotPrompt)
			waitForPrompt();
	}
	
	public void addMatch(Match match) {
		matches.add(match);
		if(!textWaiters.contains(matchQueue)) {
			textWaiters.add(matchQueue);
		}
	}
	
	public Match matchWait (double timeout) {
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
				String[] lines = text.split("\\n");
				for(String line : lines) {
					// try all of our matches
					for(Match match : matches) {
						if(match.matches(line)) {
							return match;
						}
					}
				}
			}
		} finally {
			matches.clear();
			textWaiters.remove(matchQueue);
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
	}

	public void pause (double seconds) {
		try {
			// FIXME need to make this work for multiple users
			pausedThread = Thread.currentThread();
			Thread.sleep((long)(seconds * 1000.0));
		} catch(InterruptedException e) {
			e.printStackTrace();
		} finally {
			pausedThread = null;
		}
	}
	
	public void put (String text) {
		assertPrompt();
		
		client.send("[" + scriptName + "]: ", text);
	}

	public void waitFor (Match match) {
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		String text = null;

		textWaiters.add(queue);
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
			textWaiters.remove(queue);
		}
	}

	public void waitForPrompt () {
		lock.lock();
		try {
			promptWaiters++;
			while(!interrupted && !gotPrompt) {
				gotPromptCond.await();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			promptWaiters--;
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
		if(promptWaiters > 0) {
			lock.lock();
			try {
				gotPromptCond.signalAll();
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
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
	
	protected void receiveText(String text) {
		synchronized(textWaiters) {
			for(LinkedBlockingQueue<String>  queue : textWaiters) {
				try {
					queue.put(text);
				} catch(Exception e) {
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
	}
	
	public void interrupt() {
		lock.lock();
		try {
			interrupted = true;
			gotPromptCond.signalAll();
			nextRoom.signalAll();
			if(pausedThread != null)
				pausedThread.interrupt();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void clearInterrupt() {
		interrupted = false;
	}
}
