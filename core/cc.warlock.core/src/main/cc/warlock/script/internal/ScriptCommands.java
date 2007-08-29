package cc.warlock.script.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cc.warlock.client.IProperty;
import cc.warlock.client.IPropertyListener;
import cc.warlock.client.IStream;
import cc.warlock.client.IStreamListener;
import cc.warlock.client.IStyledString;
import cc.warlock.client.internal.Command;
import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.script.CallbackEvent;
import cc.warlock.script.IScript;
import cc.warlock.script.IScriptCallback;
import cc.warlock.script.IScriptCommands;
import cc.warlock.script.Match;


public class ScriptCommands implements IScriptCommands, IStreamListener, IPropertyListener<Integer>
{

	protected IStormFrontClient client;
	protected ArrayList<IScriptCallback> callbacks = new ArrayList<IScriptCallback>();
	protected Match[] matches;
	protected String waitForText;
	protected boolean ignoreCase, regex;
	protected boolean waitingForMatches, waiting;
	protected Timer timer = new Timer();
	private final Lock lock = new ReentrantLock();
	private final Condition gotText = lock.newCondition();
	private final Condition nextRoom = lock.newCondition();
	private String text;
	private int textWaiters = 0;
	
	public ScriptCommands (IStormFrontClient client)
	{
		this.client = client;
		waiting = waitingForMatches = waitingForRoundtime = false;
		
		client.getDefaultStream().addStreamListener(this);
		client.getRoundtime().addListener(this);
	}
	
	public void echo (String text) {
		client.getDefaultStream().echo(text);
	}
	
	public void echo (IScript script, String text) {
		client.getDefaultStream().echo("[" + script.getName() + "]: " + text);
	}

	public void matchWait (Match[] matches, IScriptCallback callback) {
		matchWait(matches);
	}
	
	public Match matchWait (Match[] matches) {
		lock.lock();
		try {
			textWaiters++;
			while(true) {
				System.out.println("Waiting for text");
				gotText.await();
				if(text == null) {
					System.out.println("No text!!");
					continue;
				}
				System.out.println("Got line: " + text);
				for(Match match : matches) {
					System.out.println("Trying a match");
					if(match.matches(text)) {
						System.out.println("matched a line");
						return match;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			System.out.println("Done with matchwait");
			textWaiters--;
			lock.unlock();
		}
		return null;
	}

	public void move (String direction, IScriptCallback callback) {
		client.send(direction);
		client.getDefaultStream().echo(direction);
		nextRoom(callback);
	}

	public void nextRoom (IScriptCallback callback) {
		lock.lock();
		try {
			nextRoom.await();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void pause (int seconds, final IScriptCallback callback) {
		timer.schedule(new TimerTask() {
			public void run() {
				CallbackEvent event = new CallbackEvent(IScriptCallback.CallbackType.FinishedPausing);
				callback.handleCallback(event);
			}
		}, seconds*1000);
	}

	public void put (String text) {
		// false command so it doesn't get added to the command history
		Command command = new Command(text, new Date());
		command.setInHistory(true);
		
		client.send(command);
		client.getDefaultStream().echo(text);
	}
	
	public void put (IScript script, String text) {
		while (!client.getDefaultStream().isPrompting())
		{
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// false command so it doesn't get added to the command history
		Command command = new Command(text, new Date());
		command.setInHistory(true);
		
		client.send(command);
		client.getDefaultStream().echo("[" + script.getName() + "]: " + text);
	}

	public void waitFor (String text, boolean regex, boolean ignoreCase, IScriptCallback callback) {
		addCallback(callback);
		this.waitForText = text;
		this.regex = regex;
		this.ignoreCase = ignoreCase;
		// waitingForText = true;
	}

	public void waitForPrompt (IScriptCallback callback) {
		addCallback(callback);
		waiting = true;
		
		timer.schedule(new TimerTask () {
			public void run () {
				if (client.getDefaultStream().isPrompting())
				{
					cancel();
					waiting = false;
					sendEvent(new CallbackEvent(IScriptCallback.CallbackType.FinishedWaitingForPrompt), false);
				}
			}
		}, 200, 200);
	}
	
	protected void addCallback (IScriptCallback callback)
	{
		callbacks.add(callback);
	}
	
	public void removeCallback (IScriptCallback callback)
	{
		if (callbacks.contains(callback))
		{
			callbacks.remove(callback);
		}
	}
	
	protected void clearCallbacks ()
	{
		callbacks.clear();
	}
	
	public void sendEvent (CallbackEvent event)
	{
		sendEvent(event, true);
	}
	
	public void sendEvent (final CallbackEvent event, boolean asynch)
	{
		for (final IScriptCallback callback : callbacks) {
			if (asynch)
			{
				timer.schedule(new TimerTask() {
					public void run () {
						callback.handleCallback(event);
					}
				}, 100);
			} else {
				callback.handleCallback(event);
			}
		}
	}
	
	public IStormFrontClient getClient() {
		return this.client;
	}
		
	public void streamCleared(IStream stream) {}
	public void streamEchoed(IStream stream, String text) {}
	
	public void streamPrompted(IStream stream, String prompt) { }
	public void streamDonePrompting (IStream stream) { }
	
	public void streamReceivedText(IStream stream, IStyledString string) {
		lock.lock();
		try {
			text = string.getBuffer().toString();
			if(textWaiters > 0) {
				System.out.println("Signaling waiters");
				gotText.signalAll();
			}
		} finally {
			lock.unlock();
		}
		
		/*
		if (waitingForMatches)
		{
			Match foundMatch = null;
			for (Match match : matches)
			{
				if (match.matches(text))
				{
					foundMatch = match;
					break;
				}
			}
			
			if (foundMatch != null)
			{
				CallbackEvent event = new CallbackEvent(IScriptCallback.CallbackType.Matched);
				event.data.put(CallbackEvent.DATA_MATCH, foundMatch);
				sendEvent(event);
				
				clearCallbacks();
				waitingForMatches = false;
			}
		}
		else if (waitingForText)
		{
			if (!regex)
			{
				if (text.toUpperCase().contains(waitForText.toUpperCase()))
				{
					sendEvent(new CallbackEvent(IScriptCallback.CallbackType.FinishedWaiting));
					clearCallbacks();
					waitingForText = false;
				}
			}
		}
		*/
	}
	
	public void movedToRoom() {
		lock.lock();
		try {
			nextRoom.notifyAll();
		} finally {
			lock.unlock();
		}
	}
	
	protected boolean waitingForRoundtime;
	public void waitForRoundtime (final IScriptCallback callback)
	{
		if (client.getRoundtime().get() > 0)
		{
			waitingForRoundtime = true;
			
			timer.schedule(new TimerTask () {
				public void run() {
					if (!waitingForRoundtime)
					{
						cancel();
						CallbackEvent event = new CallbackEvent(IScriptCallback.CallbackType.FinishedWaitingForRoundtime);
						callback.handleCallback(event);
					}
				}
			}, 200, 200);
		} else {
			CallbackEvent event = new CallbackEvent(IScriptCallback.CallbackType.FinishedWaitingForRoundtime);
			callback.handleCallback(event);
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
}
