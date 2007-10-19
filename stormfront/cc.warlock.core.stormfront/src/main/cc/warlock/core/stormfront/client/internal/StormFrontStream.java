package cc.warlock.core.stormfront.client.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockStyle;
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

	protected HashMap<Integer, HighlightString> highlightIndexes = new HashMap<Integer, HighlightString>();
	protected Stack<HighlightString> fullLineStrings = new Stack<HighlightString>();
	protected HashMap<Integer, HighlightString> highlightEndIndexes = new HashMap<Integer, HighlightString>();
	
	@Override
	public void send(String text) {
		StreamEvent[] events = getHighlightEvents(text);
		
		for (StreamEvent event : events)
		{
			if (event.type == StreamEvent.Type.Text)
			{
				super.send(event.text);
			}
			else if (event.type == StreamEvent.Type.Style)
			{
				super.sendStyle(event.style);
			}
		}
	}
	
	protected static class StreamEvent {
		public String text;
		public IWarlockStyle style;
		
		public static enum Type { Text, Style };
		public Type type;
		
		public StreamEvent (String text)
		{
			this.type = Type.Text;
			this.text = text;
		}
		
		public StreamEvent (IWarlockStyle style)
		{
			this.type = Type.Style;
			this.style = style;
		}
	}
	
	protected StreamEvent[] getHighlightEvents (String text)
	{
		if (client.getServerSettings().getHighlightStrings().size() == 0)
		{
			return new StreamEvent[] { new StreamEvent(text) };
		}
		
		StringBuffer regex = new StringBuffer();
		for (Iterator<HighlightString> iter = client.getServerSettings().getHighlightStrings().iterator(); iter.hasNext(); )
		{
			HighlightString string = iter.next();
			
			regex.append(string.getText());
			if (iter.hasNext())
			{
				regex.append("|");
			}
		}
		
		ArrayList<StreamEvent> events = new ArrayList<StreamEvent>();
		getHighlightEvents(text, regex.toString(), events);
		
		return events.toArray(new StreamEvent[events.size()]);
	}
	
	protected void getHighlightEvents (String text, String regex, ArrayList<StreamEvent> events)
	{
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		String outsideTokens[] = text.split(regex);
		int surroundingIndex = 0;
		
		while (matcher.find())
		{
			if (outsideTokens.length > surroundingIndex)
				events.add(new StreamEvent(outsideTokens[surroundingIndex++]));
			
			MatchResult result = matcher.toMatchResult();
			String match = result.group();
			
			HighlightString string = client.getServerSettings().getHighlightString(match);
			events.add(new StreamEvent(new HighlightStringStyle(string)));
			
			String newRegex = regex.replaceAll("\\|" + match, "");
			newRegex = newRegex.replaceAll(match +"\\|", "");
			getHighlightEvents(match, newRegex, events);
			
			events.add(new StreamEvent(HighlightStringStyle.createEndStyle(string)));
		}
		
		if (outsideTokens.length > surroundingIndex)
			events.add(new StreamEvent(outsideTokens[surroundingIndex]));
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
