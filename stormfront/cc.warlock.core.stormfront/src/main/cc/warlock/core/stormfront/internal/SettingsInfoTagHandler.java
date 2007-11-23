package cc.warlock.core.stormfront.internal;

import java.io.File;
import java.io.IOException;

import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;
import cc.warlock.core.stormfront.xml.StormFrontDocument;


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
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		crc = attributes.getValue("crc");
		
		String major = attributes.getValue("major");
		if(major != null) {
			majorVersion = Integer.parseInt(attributes.getValue("major"));
		}
		
		clientVersion = attributes.getValue("client");
		if(clientVersion != null)
			handler.getClient().getServerSettings().setClientVersion(clientVersion);
	}
	
	@Override
	public void handleEnd(String newLine) {
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
			StormFrontDocument document = ServerSettings.getDocument(playerId);
			String currentCRC = ServerSettings.getCRC(document);
			Integer currentMajorVersion = ServerSettings.getMajorVersion(document);
			if (currentMajorVersion == null) currentMajorVersion = 0;
			
			if (currentCRC != null && crc.equals(currentCRC))
			{
				boolean sendBlankLine = true;
				
				handler.getClient().getServerSettings().load(playerId);
				// crcs match, if we have the bigger major version, override with our settings
				if (currentMajorVersion > majorVersion)
				{
					handler.getClient().getServerSettings().sendAllSettings();
					sendBlankLine = false;
				}
				
				if (sendBlankLine)
				{
					try {
						handler.getClient().getConnection().sendLine("");
					} catch(IOException e) {
						e.printStackTrace();
					}
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
	
	public Integer getMajorVersion () {
		return majorVersion;
	}
	
	public String getClientVersion () {
		return clientVersion;
	}

}
