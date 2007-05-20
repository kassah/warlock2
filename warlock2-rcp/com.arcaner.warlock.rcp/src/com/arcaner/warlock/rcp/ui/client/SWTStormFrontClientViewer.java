package com.arcaner.warlock.rcp.ui.client;

import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.client.stormfront.IStormFrontClientViewer;
import com.arcaner.warlock.configuration.server.ServerSettings;

public class SWTStormFrontClientViewer extends SWTWarlockClientViewer implements IStormFrontClientViewer
{
	private IStormFrontClientViewer viewer;
	private ListenerWrapper wrapper = new ListenerWrapper();
	
	public SWTStormFrontClientViewer (IStormFrontClientViewer viewer)
	{
		this(viewer, false);
	}
	
	public SWTStormFrontClientViewer (IStormFrontClientViewer viewer, boolean asynch)
	{
		super (viewer, asynch);
		this.viewer = viewer;
	}
	
	private static enum EventType {
		LoadServerSettings
	};
	
	private class ListenerWrapper implements Runnable
	{
		private EventType eventType;
		private ServerSettings settings;
		
		public void run() {
			switch (eventType)
			{
				case LoadServerSettings: viewer.loadServerSettings(settings); break;
			}
			
			settings = null;
		}
	}
	
	public void loadServerSettings(ServerSettings settings) {
		wrapper.settings = settings;
		wrapper.eventType = EventType.LoadServerSettings;
		run(wrapper);
	}

	public IStormFrontClient getStormFrontClient() {
		return viewer.getStormFrontClient();
	}

}
