package com.arcaner.warlock.stormfront.internal;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class CompassTagHandler extends DefaultTagHandler {

	public CompassTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "compass" };
	}
	
	@Override
	public void handleEnd() {
		handler.getClient().getScriptCommands().movedToRoom();
	}

}
