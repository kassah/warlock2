package cc.warlock.rcp.stormfront.adapters;

import java.net.URL;

import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.client.IStormFrontClientViewer;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;
import cc.warlock.rcp.ui.client.SWTWarlockClientViewer;


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
		LoadServerSettings, StartDownloadingServerSettings, ReceivedServerSetting, FinishedDownloadingServerSettings, LaunchURL
	};
	
	private class ListenerWrapper implements Runnable
	{
		private EventType eventType;
		private ServerSettings settings;
		private SettingType settingType;
		private URL url;
		
		public void run() {
			switch (eventType)
			{
				case LoadServerSettings: viewer.loadServerSettings(settings); break;
				case StartDownloadingServerSettings: viewer.startDownloadingServerSettings(); break;
				case ReceivedServerSetting: viewer.receivedServerSetting(settingType);
				case FinishedDownloadingServerSettings: viewer.finishedDownloadingServerSettings(); break;
				case LaunchURL: viewer.launchURL(url);
			}
			
			settings = null;
			url = null;
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

	public void startDownloadingServerSettings() {
		wrapper.eventType = EventType.StartDownloadingServerSettings;
		run(wrapper);
	}
	
	public void receivedServerSetting(SettingType settingType) {
		wrapper.eventType = EventType.ReceivedServerSetting;
		wrapper.settingType = settingType;
		run(wrapper);
	}
	
	public void finishedDownloadingServerSettings() {
		wrapper.eventType = EventType.FinishedDownloadingServerSettings;
		run(wrapper);
	}
	
	public void launchURL(URL url) {
		wrapper.eventType = EventType.LaunchURL;
		wrapper.url = url;
		run(wrapper);
	}
}
