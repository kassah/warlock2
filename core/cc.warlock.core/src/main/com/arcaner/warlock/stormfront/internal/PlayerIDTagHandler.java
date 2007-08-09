package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

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
		handler.getClient().getPlayerId().set(attributes.getValue("id"));
	}

}
