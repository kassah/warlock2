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
		super(client, streamName);
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
		if (client != null)
		{
			for (IgnoreSetting ignore : client.getServerSettings().getIgnores())
			{
				String str = text.toString();
				String ignoreText = ignore.getText();
				if (str.contains(ignoreText))
				{
					int pos = str.lastIndexOf(ignoreText);
					int start = str.substring(0, pos).lastIndexOf('\n');
					int end = str.indexOf('\n', pos);
					WarlockString newText = text.substring(0, start);
					newText.append(text.substring(end));
					System.out.println("Ignore matched text in: " + text.toString());
					text = newText;
				}
			}
		}
		
		super.send(text);
	}
}
