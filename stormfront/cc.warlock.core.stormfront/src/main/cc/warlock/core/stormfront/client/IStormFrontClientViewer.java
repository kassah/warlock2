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
package cc.warlock.core.stormfront.client;

import java.net.URL;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;


public interface IStormFrontClientViewer extends IWarlockClientViewer {

	public enum SettingType {
		Presets, Strings, Streams, Scripts, Names, Macros, Palette, Vars, Dialogs, Panels, Toggles, Misc, Options;
		
		public String toString ()
		{
			switch (this) {
			case Presets: return "Presets";
			case Strings: return "Strings";
			case Streams: return "Streams";
			case Scripts: return "Scripts";
			case Names: return "Names";
			case Macros: return "Macros";
			case Palette: return "Palette";
			case Vars: return "Variables";
			case Dialogs: return "Dialogs";
			case Panels: return "Panels";
			case Toggles: return "Toggles";
			case Misc: return "Misc";
			case Options: return "Options";
			}
			
			return super.toString();
		}
	};
	
	public IStormFrontClient getStormFrontClient ();
	
	public void startDownloadingServerSettings();
	public void receivedServerSetting(SettingType settingType);
	public void finishedDownloadingServerSettings();
	
	public void loadServerSettings(ServerSettings settings);

	/**
	 * Append an image to this viewer
	 * @param imageURL The URL of the image to show
	 */
	public void appendImage (URL imageURL);
	
	/**
	 * Launch a URL
	 * @param url The URL to launch
	 */
	public void launchURL (URL url);
}
