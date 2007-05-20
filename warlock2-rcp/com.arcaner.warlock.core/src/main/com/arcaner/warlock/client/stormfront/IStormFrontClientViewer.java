package com.arcaner.warlock.client.stormfront;

import com.arcaner.warlock.client.IWarlockClientViewer;
import com.arcaner.warlock.configuration.server.ServerSettings;

public interface IStormFrontClientViewer extends IWarlockClientViewer {

	public IStormFrontClient getStormFrontClient ();
	
	public void loadServerSettings(ServerSettings settings);
}
