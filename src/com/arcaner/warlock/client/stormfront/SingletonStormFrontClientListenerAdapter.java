/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.client.stormfront;

import java.util.ArrayList;
import java.util.HashMap;

import com.arcaner.warlock.client.IWarlockClient;

/**
 * @author Marshall
 *
 * Holds common functionality for singleton Storm Front Client Listeners. 
 */
public class SingletonStormFrontClientListenerAdapter implements IStormFrontClientListener {
	
	private IWarlockClient active;
	private ArrayList<IWarlockClient> clients;
	private HashMap<IWarlockClient, HashMap<String, Object>> clientProperties;
	private IStormFrontClientListener listener;
	
	private static final String PROP_FATIGUE = "fatigue";
	private static final String PROP_FATIGUE_LABEL = "fatigue-label";
	private static final String PROP_HEALTH = "health";
	private static final String PROP_HEALTH_LABEL = "health-label";
	private static final String PROP_MANA = "mana";
	private static final String PROP_MANA_LABEL = "mana-label";
	private static final String PROP_ROUNDTIME = "roundtime";
	private static final String PROP_SPIRIT = "spirit";
	private static final String PROP_SPIRIT_LABEL = "spirit-label";
	
	public SingletonStormFrontClientListenerAdapter (IStormFrontClientListener listener)
	{
		clients = new ArrayList<IWarlockClient>();
		clientProperties = new HashMap<IWarlockClient, HashMap<String,Object>>();
		this.listener = listener;
	}
	
	public void addWarlockClient(IWarlockClient client) {
		clients.add(client);
	}
	
	public HashMap<String,Object> getClientProperties (IWarlockClient client) {
		HashMap<String,Object> props = clientProperties.get(client);
		if (props == null) {
			props = new HashMap<String,Object>();
			clientProperties.put(client, props);
		}
		
		return props;
	}
	
	public Object activeProperty (String propId)
	{
		return getClientProperties(active).get(propId);
	}
	
	public void setActiveClient(IWarlockClient active) {
		this.active = active;
		
		if (activeProperty(PROP_FATIGUE) != null)
			listener.fatigueChanged(active, (Integer) activeProperty(PROP_FATIGUE), (String) activeProperty(PROP_FATIGUE_LABEL));
		
		if (activeProperty(PROP_HEALTH) != null)
			listener.healthChanged(active, (Integer) activeProperty(PROP_HEALTH), (String) activeProperty(PROP_HEALTH_LABEL));
		
		if (activeProperty(PROP_MANA) != null)
			listener.manaChanged(active, (Integer) activeProperty(PROP_MANA), (String) activeProperty(PROP_MANA_LABEL));
		
		if (activeProperty(PROP_ROUNDTIME) != null)
			listener.roundtimeChanged(active, (Integer) activeProperty(PROP_ROUNDTIME));
		
		if (activeProperty(PROP_SPIRIT) != null)
			listener.spiritChanged(active, (Integer) activeProperty(PROP_SPIRIT), (String) activeProperty(PROP_SPIRIT_LABEL));
	}

	public void fatigueChanged(IWarlockClient source, int fatigue, String label) {
		getClientProperties(source).put(PROP_FATIGUE, fatigue);
		getClientProperties(source).put(PROP_FATIGUE_LABEL, label);
		
		if (source == active)
		{
			listener.fatigueChanged(source, fatigue, label);
		}
	}

	public void healthChanged(IWarlockClient source, int health, String label) {
		getClientProperties(source).put(PROP_HEALTH, health);
		getClientProperties(source).put(PROP_HEALTH_LABEL, label);
		
		if (source == active)
		{
			listener.healthChanged(source, health, label);
		}
	}

	public void manaChanged(IWarlockClient source, int mana, String label) {
		getClientProperties(source).put(PROP_MANA, mana);
		getClientProperties(source).put(PROP_MANA_LABEL, label);
		
		if (source == active)
		{
			listener.manaChanged(source, mana, label);
		}
	}

	public void roundtimeChanged(IWarlockClient source, int roundtime) {
		getClientProperties(source).put(PROP_ROUNDTIME, roundtime);
		
		if (source == active)
		{
			listener.roundtimeChanged(source, roundtime);
		}

	}

	public void roundtimeStarted(IWarlockClient source, int roundtime) {
		if (source == active)
		{
			listener.roundtimeStarted(source, roundtime);
		}
	}

	public void spiritChanged(IWarlockClient source, int spirit, String label) {
		getClientProperties(source).put(PROP_SPIRIT, spirit);
		getClientProperties(source).put(PROP_SPIRIT_LABEL, label);
		
		if (source == active)
		{
			listener.spiritChanged(source, spirit, label);
		}
	}
	
}
