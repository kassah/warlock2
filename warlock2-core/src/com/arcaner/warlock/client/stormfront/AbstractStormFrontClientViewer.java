package com.arcaner.warlock.client.stormfront;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.stormfront.internal.StormFrontStyle;

public abstract class AbstractStormFrontClientViewer implements IStormFrontClientViewer {

	protected IStormFrontClient client;
	
	public AbstractStormFrontClientViewer (IStormFrontClient client)
	{
		this.client = client;
	}
	
	public IStormFrontClient getStormFrontClient() {
		return client;
	}

	public void append(String viewName, String text) {
		append (viewName, text, StormFrontStyle.EMPTY_STYLE);
	}
	
	public void echo(String viewName, String text) {
		echo (viewName, text, StormFrontStyle.EMPTY_STYLE);
	}

	public IWarlockClient getWarlockClient() {
		return getStormFrontClient();
	}

}
