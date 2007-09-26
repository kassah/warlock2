package cc.warlock.core.stormfront.script.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.Match;
import cc.warlock.core.script.internal.ScriptCommands;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.script.IStormFrontScriptCommands;

public class StormFrontScriptCommands extends ScriptCommands implements IStormFrontScriptCommands, IPropertyListener<Integer> {

	protected IStormFrontClient sfClient;
	protected IScript script;
	protected List<ScriptAction> actions;
	
	public StormFrontScriptCommands (IStormFrontClient client, IScript script)
	{
		super(client);
		this.sfClient = client;
		waitingForRoundtime = false;
		this.script = script;
		
		client.getRoundtime().addListener(this);
	}
	
	public IStormFrontClient getStormFrontClient() {
		return sfClient;
	}
	
	protected boolean waitingForRoundtime;
	public void waitForRoundtime ()
	{
		if(!stopped) {
			assertPrompt();
			try {
				while(sfClient.getRoundtime().get() > 0 && !stopped) {
					Thread.sleep((sfClient.getRoundtime().get() + 1) * 1000);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void nextRoom() {
		try {
			super.nextRoom();
		} finally {
			waitForRoundtime();
		}
	}
	
	@Override
	public void pause(int seconds) {
		try {
			super.pause(seconds);
		} finally {
			waitForRoundtime();
		}
	}
	
	@Override
	public void waitFor(Match match) {
		try {
			super.waitFor(match);
		} finally {
			waitForRoundtime();
		}
	}
	
	@Override
	public void waitForPrompt() {
		try {
			super.waitForPrompt();
		} finally {
			waitForRoundtime();
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
	
	protected class ScriptAction {
		
		private String action;
		private Pattern regex;
		private String name;
		
		ScriptAction(String action, String regex) {
			this.regex = Pattern.compile(regex);
			this.name = regex;
			this.action = action;
		}
		
		boolean match(String text) {
			Matcher m = regex.matcher(text);
			if(m.matches()) {
				script.execute(action);
				return true;
			}
			return false;
		}
		
		String getName() {
			return name;
		}
	}
	
	protected  class ScriptActionThread implements Runnable {
		public void run() {
			LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
			textWaiters.add(queue);
			actionLoop: while(actions != null) {
				String text = null;

				while(text == null && actions != null) {
					try {
						text = queue.poll(100L, TimeUnit.MILLISECONDS);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				for(ScriptAction action : actions) {
					if(action.match(text)) {
						break actionLoop;
					}
				}
			}
			textWaiters.remove(queue);
		}
	}
	
	public void addAction(String action, String text) {
		if(actions == null) {
			actions = Collections.synchronizedList(new ArrayList<ScriptAction>());
			new Thread(new ScriptActionThread()).start();
		}
		actions.add(new ScriptAction(action, text));
	}
	
	public void clearActions() {
		actions = null;
	}
	
	public void removeAction(String text) {
		Iterator<ScriptAction> iter = actions.iterator();
		while(iter.hasNext()) {
			// remove the element with the same name as text
			if(iter.next().getName().equals(text)) {
				iter.remove();
			}
		}
		if(actions.size() == 0) {
			actions = null;
		}
	}
	
}
