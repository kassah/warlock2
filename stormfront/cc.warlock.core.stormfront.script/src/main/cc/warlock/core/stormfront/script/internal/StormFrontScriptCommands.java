package cc.warlock.core.stormfront.script.internal;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.script.Match;
import cc.warlock.core.script.internal.ScriptCommands;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.script.IStormFrontScriptCommands;

public class StormFrontScriptCommands extends ScriptCommands implements IStormFrontScriptCommands, IPropertyListener<Integer> {

	protected IStormFrontClient sfClient;
	
	public StormFrontScriptCommands (IStormFrontClient client)
	{
		super(client);
		this.sfClient = client;
		waitingForRoundtime = false;
		
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
}
