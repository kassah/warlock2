package cc.warlock.core.stormfront.client.internal;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.internal.Stream;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.serversettings.server.IgnoreSetting;

public class StormFrontStream extends Stream {

	protected IStormFrontClient client;
	
	protected StormFrontStream (IStormFrontClient client, String streamName)
	{
		super(streamName);
		
		this.client = client;
	}
	
	protected static Stream fromNameAndClient (IStormFrontClient client, String name)
	{
		if (streams.containsKey(name))
			return streams.get(name);
		
		else return new StormFrontStream(client, name);
	}
	
	@Override
	public IWarlockClient getClient() {
		return client;
	}
	
	@Override
	public void send(WarlockString text) {
		
		boolean ignored = false;
		
		for (IgnoreSetting ignore : client.getServerSettings().getIgnores())
		{
			if (!text.toString().contains(ignore.getText()))
			{
				ignored = true; break;
			}
		}
		
		if (!ignored) {
			super.send(text);
		}
	}
}
