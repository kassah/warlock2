package com.arcaner.warlock.stormfront.internal;

import java.io.File;

import com.arcaner.warlock.configuration.WarlockConfiguration;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class SettingsInfoTagHandler extends DefaultTagHandler {

	public SettingsInfoTagHandler(IStormFrontProtocolHandler handler) {
		super (handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "settingsInfo" };
	}
	
	@Override
	public void handleEnd() {
		String playerId = handler.getClient().getPlayerId();
		
		File serverSettings = WarlockConfiguration.getConfigurationFile("serverSettings_" + playerId + ".xml", false);
		if (!serverSettings.exists())
		{
			handler.getClient().send("<sendSettings/>");
		} else {
			// no-op
			handler.getClient().send("");
		}
	}

}
