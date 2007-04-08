package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.client.stormfront.internal.StormFrontClient;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class PlayerIDTagHandler extends DefaultTagHandler {

	public PlayerIDTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "playerID" };
	}
	
	@Override
	public void handleStart(Attributes attributes) {
		((StormFrontClient)handler.getClient()).setPlayerId(attributes.getValue("id"));
	}

}
