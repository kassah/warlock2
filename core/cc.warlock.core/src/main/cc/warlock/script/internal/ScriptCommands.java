package cc.warlock.script.internal;

import java.util.ArrayList;

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
		callbacks.add(callback);
		this.matches = matches;
		waitingForMatches = true;
	}

	public void move (String direction, IScriptCallback callback) {
		client.send(direction);
		client.getDefaultStream().echo(direction);
		nextRoom(callback);
	}

	public void nextRoom (IScriptCallback callback) {
		callbacks.add(callback);
		waitingForRoom = true;
	}

	public void pause (int seconds) {
		try {
			Thread.sleep(1000 * seconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waitForRoundtime();
	}

	public void put (String text) {
		client.send(text);
		client.getDefaultStream().echo(text);
	}
	
	public void put (IScript script, String text) {
		client.send(text);
		client.getDefaultStream().echo("[" + script.getName() + "]: " + text);
	}

	public void waitFor (String text, boolean regex, boolean ignoreCase, IScriptCallback callback) {
		callbacks.add(callback);
		this.waitForText = text;
		this.regex = regex;
		this.ignoreCase = ignoreCase;
		waitingForText = true;
	}

	public void waitForLine (IScriptCallback callback) {
		callbacks.add(callback);
		waiting = true;
	}
	
	public IStormFrontClient getClient() {
		return this.client;
	}
	
	public void removeCallback(IScriptCallback callback) {
		if (callbacks.contains(callback)) callbacks.remove(callback);
	}
	
	public void streamCleared(IStream stream) {}
	public void streamEchoed(IStream stream, String text) {}
	public void streamPrompted(IStream stream, String prompt) {}
	
	public void streamReceivedText(IStream stream, IStyledString string) {
		String text = string.getBuffer().toString();
		
		if (waiting)
		{
			CallbackEvent event = new CallbackEvent(IScriptCallback.CallbackType.FinishedWaiting);
			for (IScriptCallback callback : callbacks) callback.handleCallback(event);
			callbacks.clear();
			waiting = false;
		}
		else if (waitingForMatches)
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
				for (IScriptCallback callback : callbacks)
				{
					callback.handleCallback(event);
				}
				
				callbacks.clear();
				waitingForMatches = false;
			}
		}
		else if (waitingForText)
		{
			if (!regex)
			{
				if (text.contains(waitForText))
				{
					CallbackEvent event = new CallbackEvent(IScriptCallback.CallbackType.FinishedWaiting);
					for (IScriptCallback callback : callbacks) callback.handleCallback(event);
					callbacks.clear();
					waitingForText = false;
				}
			}
		}
	}
	
	public void movedToRoom() {
		if (waitingForRoom)
		{
			CallbackEvent event = new CallbackEvent(IScriptCallback.CallbackType.InNextRoom);
			for (IScriptCallback callback : callbacks) callback.handleCallback(event);
			callbacks.clear();
			waitingForRoom = false;
		}
	}
	
	protected boolean waitingForRoundtime;
	protected void waitForRoundtime ()
	{
		if (client.getRoundtime().get() > 0)
		{
			waitingForRoundtime = true;
			while (waitingForRoundtime)
			{
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
}
