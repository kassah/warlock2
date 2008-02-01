/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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
		super (viewer);
		this.viewer = viewer;
	}
	
	private static enum EventType {
		LoadServerSettings, StartDownloadingServerSettings, ReceivedServerSetting,
		FinishedDownloadingServerSettings, LaunchURL, AppendImage
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
				case LaunchURL: viewer.launchURL(url); break;
				case AppendImage: viewer.appendImage(url); break;
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
	
	public void appendImage(URL imageURL) {
		wrapper.eventType = EventType.AppendImage;
		wrapper.url = imageURL;
		run(wrapper);
	}
}
