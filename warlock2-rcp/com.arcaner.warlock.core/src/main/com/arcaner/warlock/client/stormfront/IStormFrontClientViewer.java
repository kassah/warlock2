package com.arcaner.warlock.client.stormfront;

import com.arcaner.warlock.client.IWarlockClientViewer;
import com.arcaner.warlock.configuration.ServerSettings;

public interface IStormFrontClientViewer extends IWarlockClientViewer {

	public void append (String viewName, String text, IStormFrontStyle style);
	
	public void echo (String viewName, String text, IStormFrontStyle style);
	
	public IStormFrontClient getStormFrontClient ();
	
	public void loadServerSettings(ServerSettings settings);
}
