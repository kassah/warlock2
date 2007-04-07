package com.arcaner.warlock.stormfront.internal;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class SettingsInfoTagHandler extends DefaultTagHandler {

	public SettingsInfoTagHandler(IStormFrontProtocolHandler handler) {
		super (handler);
	}
	
	@Override
	public String getName() {
		return "settingsInfo";
	}
	
	@Override
	public void handleEnd() {
		handler.getClient().send("");
	}

}
