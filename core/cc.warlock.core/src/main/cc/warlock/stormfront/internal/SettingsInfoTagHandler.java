package cc.warlock.stormfront.internal;

import java.io.File;
import java.util.Map;

import cc.warlock.configuration.WarlockConfiguration;
import cc.warlock.configuration.server.ServerSettings;
import cc.warlock.stormfront.IStormFrontProtocolHandler;


public class SettingsInfoTagHandler extends DefaultTagHandler {

	protected String crc;
	
	public SettingsInfoTagHandler(IStormFrontProtocolHandler handler) {
		super (handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "settingsInfo" };
	}
	
	@Override
	public void handleStart(Map<String, String> attributes) {
		crc = attributes.get("crc");
	}
	
	@Override
	public void handleEnd() {
		String playerId = handler.getClient().getPlayerId().get();
		
		File serverSettings = WarlockConfiguration.getConfigurationFile("serverSettings_" + playerId + ".xml", false);
		if (!serverSettings.exists())
		{
			handler.getClient().send("<sendSettings/>");
		} else {
			// check against crc to see if we're up to date
			String currentCRC = ServerSettings.getCRC(playerId);
			
			if (currentCRC != null && crc.equals(currentCRC))
			{
				handler.getClient().getServerSettings().load(playerId);
				handler.getClient().send("");
			} else {
				System.out.println("our crc is: " + currentCRC + ", their crc is: " + crc);
				handler.getClient().send("<sendSettings/>");
			}
		}
	}
	
	public String getCRC () {
		return crc;
	}

}
