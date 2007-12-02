package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.client.internal.StormFrontClient;
import cc.warlock.core.stormfront.network.StormFrontConnection;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class ModeTagHandler extends DefaultTagHandler {
	private String id;

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
		id = attributes.getValue("id");
			
	}
	
	@Override
	public void handleEnd(String newLine) {
		if(id != null) {
			StormFrontClient client = (StormFrontClient) handler.getClient();

			if (id.equals("GAME"))
			{
				client.getGameMode().set(IStormFrontClient.GameMode.Game);
			}
			else if (id.equals("CMGR"))
			{
				client.getGameMode().set(IStormFrontClient.GameMode.CharacterManager);
				((StormFrontConnection)client.getConnection()).passThrough();
				//client.getDefaultStream().send(((StormFrontConnection)client.getConnection()).getBufferContents());
			}
		}
	}
}
