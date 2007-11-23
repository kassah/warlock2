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
	 * Launch a URL
	 * @param url The URL to launch
	 */
	public void launchURL (URL url);
}
