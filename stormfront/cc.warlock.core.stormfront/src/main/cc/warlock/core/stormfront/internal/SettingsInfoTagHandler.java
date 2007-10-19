package cc.warlock.core.stormfront.internal;

import java.io.File;
import java.io.IOException;

import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class SettingsInfoTagHandler extends DefaultTagHandler {

	protected String crc, clientVersion;
	protected int majorVersion;
	
	public SettingsInfoTagHandler(IStormFrontProtocolHandler handler) {
		super (handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "settingsInfo" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes) {
		crc = attributes.getValue("crc");
		
		String major = attributes.getValue("major");
		if(major != null) {
			majorVersion = Integer.parseInt(attributes.getValue("major"));
			handler.getClient().getServerSettings().setMajorVersion(majorVersion);
		}
		
		clientVersion = attributes.getValue("client");
		if(clientVersion != null)
			handler.getClient().getServerSettings().setClientVersion(clientVersion);
	}
	
	@Override
	public void handleEnd() {
		String playerId = handler.getClient().getPlayerId().get();
		
		File serverSettings = ConfigurationUtil.getConfigurationFile("serverSettings_" + playerId + ".xml", false);
		if (!serverSettings.exists())
		{
			try {
				handler.getClient().getConnection().send("<sendSettings/>\n");
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			// check against crc to see if we're up to date
			String currentCRC = ServerSettings.getCRC(playerId);
			
			if (currentCRC != null && crc.equals(currentCRC))
			{
				handler.getClient().getServerSettings().load(playerId);
				try {
					handler.getClient().getConnection().sendLine("");
				} catch(IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("our crc is: " + currentCRC + ", their crc is: " + crc);
				try {
					handler.getClient().getConnection().send("<sendSettings/>\n");
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getCRC () {
		return crc;
	}

}
