package cc.warlock.core.stormfront.client.internal;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.internal.Stream;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.serversettings.server.HighlightString;
import cc.warlock.core.stormfront.style.HighlightStringStyle;

public class StormFrontStream extends Stream {

	protected IStormFrontClient client;
	
	protected StormFrontStream (IStormFrontClient client, String streamName)
	{
		super(streamName);
		
		this.client = client;
	}
	
	@Override
	public void send(WarlockString text) {
		highlightText(text);
		super.send(text);
	}
	
	protected void highlightText (WarlockString text)
	{	
		for (HighlightString hstring : client.getServerSettings().getHighlightStrings())
		{
			findHighlight(hstring, text);
		}
	}
	
	protected void findHighlight (HighlightString highlight, WarlockString text)
	{
		Matcher matcher = highlight.getPattern().matcher(text.toString());
		
		while (matcher.find())
		{
			MatchResult result = matcher.toMatchResult();
			int start = result.start();
			int length = result.end() - start;
			
			IWarlockStyle style = new HighlightStringStyle(highlight);
			style.setFGColor(highlight.getForegroundColor());
			style.setBGColor(highlight.getBackgroundColor());
			text.addStyle(start, length, style);
		}
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
