/*
 * Created on Jan 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.client.stormfront;

import com.arcaner.warlock.client.IWarlockClient;

/**
 * @author Marshall
 * 
 * A warlock client listener receives events for updates on the internal model of an IWarlockClient
 */
public interface IStormFrontClientListener {

	public void addWarlockClient (IWarlockClient client);
	public void setActiveClient (IWarlockClient active);
	
	public void healthChanged (IWarlockClient source, int health, String label);
	public void manaChanged (IWarlockClient source, int mana, String label);
	public void fatigueChanged (IWarlockClient source, int fatigue, String label);
	public void spiritChanged (IWarlockClient source, int spirit, String label);
	
	public void roundtimeStarted (IWarlockClient source, int roundtime);
	public void roundtimeChanged (IWarlockClient source, int roundtime);
	
}
