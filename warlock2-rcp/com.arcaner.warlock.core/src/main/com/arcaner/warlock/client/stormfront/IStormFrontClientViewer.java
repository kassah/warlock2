package com.arcaner.warlock.client.stormfront;

import com.arcaner.warlock.client.IWarlockClientViewer;
import com.arcaner.warlock.configuration.ServerSettings;
import com.arcaner.warlock.stormfront.IStream;

public interface IStormFrontClientViewer extends IWarlockClientViewer {

	public void append (IStream stream, String text, IStormFrontStyle style);
	
	public void echo (IStream stream, String text, IStormFrontStyle style);
	
	public IStormFrontClient getStormFrontClient ();
	
	public void loadServerSettings(ServerSettings settings);
}
