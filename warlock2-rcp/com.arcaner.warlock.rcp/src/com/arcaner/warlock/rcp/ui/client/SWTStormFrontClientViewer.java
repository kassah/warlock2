package com.arcaner.warlock.rcp.ui.client;

import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.client.stormfront.IStormFrontClientViewer;
import com.arcaner.warlock.client.stormfront.IStormFrontStyle;
import com.arcaner.warlock.configuration.ServerSettings;

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
		Append, Echo, LoadServerSettings
	};
	
	private class ListenerWrapper implements Runnable
	{
		private EventType eventType;
		private String viewName, text;
		private IStormFrontStyle style;
		private ServerSettings settings;
		
		public void run() {
			switch (eventType)
			{
				case Append: viewer.append(viewName, text, style); break;
				case Echo: viewer.echo(viewName, text, style); break;
				case LoadServerSettings: viewer.loadServerSettings(settings); break;
			}
			
			viewName = text = null;
			style = null;
			settings = null;
		}
	}
	
	public void append(String viewName, String text, IStormFrontStyle style) {
		wrapper.viewName = viewName;
		wrapper.text = text;
		wrapper.style = style;
		wrapper.eventType = EventType.Append;
		run(wrapper);
	}

	public void echo(String viewName, String text, IStormFrontStyle style) {
		wrapper.viewName = viewName;
		wrapper.text = text;
		wrapper.style = style;
		wrapper.eventType = EventType.Echo;
		run(wrapper);
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
