package cc.warlock.core.stormfront.client.internal;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.internal.Stream;
import cc.warlock.core.stormfront.client.IStormFrontClient;

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
}
