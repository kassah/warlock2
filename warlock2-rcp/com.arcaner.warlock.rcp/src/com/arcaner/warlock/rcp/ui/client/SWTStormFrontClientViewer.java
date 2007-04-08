package com.arcaner.warlock.rcp.ui.client;

import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.client.stormfront.IStormFrontClientViewer;
import com.arcaner.warlock.client.stormfront.IStormFrontStyle;
import com.arcaner.warlock.configuration.ServerSettings;
import com.arcaner.warlock.stormfront.IStream;

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
		private IStream stream;
		private String text;
		private IStormFrontStyle style;
		private ServerSettings settings;
		
		public void run() {
			switch (eventType)
			{
				case Append: viewer.append(stream, text, style); break;
				case Echo: viewer.echo(stream, text, style); break;
				case LoadServerSettings: viewer.loadServerSettings(settings); break;
			}
			
			text = null;
			style = null;
			settings = null;
			stream = null;
		}
	}
	
	public void append(IStream stream, String text, IStormFrontStyle style) {
		wrapper.stream = stream;
		wrapper.text = text;
		wrapper.style = style;
		wrapper.eventType = EventType.Append;
		run(wrapper);
	}

	public void echo(IStream stream, String text, IStormFrontStyle style) {
		wrapper.stream = stream;
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
