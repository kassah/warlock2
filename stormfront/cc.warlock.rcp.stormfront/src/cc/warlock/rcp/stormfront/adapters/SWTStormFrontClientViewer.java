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
import cc.warlock.rcp.ui.client.SWTWarlockClientViewer;


public class SWTStormFrontClientViewer extends SWTWarlockClientViewer implements IStormFrontClientViewer
{
	private IStormFrontClientViewer sfViewer;
	
	public SWTStormFrontClientViewer (IStormFrontClientViewer viewer)
	{
		super (viewer);
		this.sfViewer = viewer;
	}
	
	private class StartedDownloadWrapper implements Runnable
	{
		public void run() {
			sfViewer.startedDownloadingServerSettings();
		}
	}

	private class ReceivedSettingWrapper implements Runnable
	{
		private String setting;
		
		public ReceivedSettingWrapper(String setting) {
			this.setting = setting;
		}

		public void run() {
			sfViewer.receivedServerSetting(setting);
		}
	}

	private class FinishedDownloadWrapper implements Runnable
	{
		public void run() {
			sfViewer.finishedDownloadingServerSettings();
		}
	}
	
	private class LaunchUrlWrapper implements Runnable
	{
		private URL url;

		LaunchUrlWrapper(URL url) {
			this.url = url;
		}
		
		public void run() {
			sfViewer.launchURL(url);
		}
	}
	
	private class AppendImageWrapper implements Runnable
	{
		private URL url;

		public AppendImageWrapper(URL url) {
			this.url = url;
		}
		
		public void run() {
			sfViewer.appendImage(url);
		}
	}
	
	public IStormFrontClient getStormFrontClient() {
		return sfViewer.getStormFrontClient();
	}

	public void startedDownloadingServerSettings() {
		run(new StartedDownloadWrapper());
	}
	
	public void receivedServerSetting(String setting) {
		run(new ReceivedSettingWrapper(setting));
	}
	
	public void finishedDownloadingServerSettings() {
		run(new FinishedDownloadWrapper());
	}
	
	public void launchURL(URL url) {
		run(new LaunchUrlWrapper(url));
	}
	
	public void appendImage(URL imageURL) {
		run(new AppendImageWrapper(imageURL));
	}
}
