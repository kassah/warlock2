package cc.warlock.script.internal;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cc.warlock.client.IProperty;
import cc.warlock.client.IPropertyListener;
import cc.warlock.client.IStream;
import cc.warlock.client.IStreamListener;
import cc.warlock.client.IStyledString;
import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.script.CallbackEvent;
import cc.warlock.script.IMatch;
import cc.warlock.script.IScript;
import cc.warlock.script.IScriptCallback;
import cc.warlock.script.IScriptCommands;


public class ScriptCommands implements IScriptCommands, IStreamListener, IPropertyListener<Integer>
{

	protected IStormFrontClient client;
	protected ArrayList<IScriptCallback> callbacks = new ArrayList<IScriptCallback>();
	protected IMatch[] matches;
	protected String waitForText;
	protected boolean ignoreCase, regex;
	protected boolean waitingForMatches, waitingForRoom, waitingForText, waiting;
	protected Timer timer = new Timer();
	
	public ScriptCommands (IStormFrontClient client)
	{
		this.client = client;
		waiting = waitingForMatches = waitingForRoom = waitingForText = waitingForRoundtime = false;
		
		client.getDefaultStream().addStreamListener(this);
		client.getRoundtime().addListener(this);
	}
	
	public void echo (String text) {
		client.getDefaultStream().echo(text);
	}
	
	public void echo (IScript script, String text) {
		client.getDefaultStream().echo("[" + script.getName() + "]: " + text);
	}

	public void matchWait (IMatch[] matches, IScriptCallback callback) {
		addCallback(callback);
		this.matches = matches;
		waitingForMatches = true;
	}

	public void move (String direction, IScriptCallback callback) {
		client.send(direction);
		client.getDefaultStream().echo(direction);
		nextRoom(callback);
	}

	public void nextRoom (IScriptCallback callback) {
		addCallback(callback);
		waitingForRoom = true;
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
		client.send(text);
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
		
		client.send(text);
		client.getDefaultStream().echo("[" + script.getName() + "]: " + text);
	}

	public void waitFor (String text, boolean regex, boolean ignoreCase, IScriptCallback callback) {
		addCallback(callback);
		this.waitForText = text;
		this.regex = regex;
		this.ignoreCase = ignoreCase;
		waitingForText = true;
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
	
	public void streamReceivedText(IStream stream, IStyledString string) {
		String text = string.getBuffer().toString();
		
		if (waitingForMatches)
		{
			IMatch foundMatch = null;
			for (IMatch match : matches)
			{
				if (!match.isRegex())
				{
					if (text.contains(match.getMatchText()))
					{
						foundMatch = match; break;
					}
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
	}
	
	public void movedToRoom() {
		if (waitingForRoom)
		{
			sendEvent(new CallbackEvent(IScriptCallback.CallbackType.InNextRoom));
			clearCallbacks();
			waitingForRoom = false;
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
