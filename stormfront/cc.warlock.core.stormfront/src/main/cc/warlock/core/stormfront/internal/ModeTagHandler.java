package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.client.internal.StormFrontClient;
import cc.warlock.core.stormfront.network.StormFrontConnection;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class ModeTagHandler extends DefaultTagHandler {

	public ModeTagHandler (IStormFrontProtocolHandler handler)
	{
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "mode" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		if (attributes.getValue("id") != null)
		{
			StormFrontClient client = (StormFrontClient) handler.getClient();
			
			if (attributes.getValue("id").equals("GAME"))
			{
				client.getGameMode().set(IStormFrontClient.GameMode.Game);
			}
			else if (attributes.getValue("id").equals("CMGR"))
			{
				client.getGameMode().set(IStormFrontClient.GameMode.CharacterManager);
				client.getDefaultStream().send(((StormFrontConnection)client.getConnection()).getBufferContents());
			}
		}
	}
}
