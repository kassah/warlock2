package cc.warlock.core.stormfront.client.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import cc.warlock.core.client.IWarlockClient;
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
		// do automatic highlighting -- break up the string into style and text events
		
		highlightIndexes.clear();
		highlightEndIndexes.clear();
		
		for (HighlightString string : client.getServerSettings().getHighlightStrings())
		{
			int index = text.indexOf(string.getText());
			if (index > -1)
			{
				highlightIndexes.put(index, string);
				if (!string.isFillEntireLine())
				{
					highlightEndIndexes.put(index + string.getText().length(), string);
				}
			}
		}
		
		if (highlightIndexes.keySet().size() == 0) { super.send(text); return; }

		// first send all "fillEntireLine" highlights, remove them from the main list, and cache them so we can send "end styles" after
		fullLineStrings.clear();
		for (Iterator<Map.Entry<Integer,HighlightString>> iter = highlightIndexes.entrySet().iterator(); iter.hasNext();)
		{
			Map.Entry<Integer,HighlightString> entry = iter.next();
			if (entry.getValue().isFillEntireLine())
			{
				super.sendStyle(new HighlightStringStyle(entry.getValue()));
				fullLineStrings.push(entry.getValue());
				iter.remove();
			}
		}
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		ArrayList<Integer> endIndexes = new ArrayList<Integer>();
		
		indexes.addAll(Arrays.asList(highlightIndexes.keySet().toArray(new Integer[highlightIndexes.keySet().size()])));
		endIndexes.addAll(Arrays.asList(highlightEndIndexes.keySet().toArray(new Integer[highlightEndIndexes.keySet().size()])));
		Collections.sort(indexes);
		Collections.sort(endIndexes);
		
		if (indexes.size() > 0)
		{
			int currentIndex = 0;
			for (Integer index : indexes)
			{
				for (Iterator<Integer> iter = endIndexes.iterator(); iter.hasNext(); )
				{
					Integer endIndex = iter.next();
					if (endIndex <= currentIndex + index)
					{
						if (endIndex > currentIndex)
						{
							super.send(text.substring(currentIndex, endIndex));
							currentIndex = endIndex;
						}
						super.sendStyle(HighlightStringStyle.createEndStyle(highlightEndIndexes.get(endIndex)));
						iter.remove();
					}
				}
				
				// send the text "before" this highlight
				if (index > currentIndex)
				{
					super.send(text.substring(currentIndex, index));
					
					currentIndex = index;
				}
				
				// send the highlight itself
				super.sendStyle(new HighlightStringStyle(highlightIndexes.get(index)));
			}
			
			if (currentIndex < text.length() - 1)
			{
				// send the remainder text
				for (Iterator<Integer> iter = endIndexes.iterator(); iter.hasNext(); )
				{
					Integer endIndex = iter.next();
					if (endIndex <= text.length() - 1)
					{
						if (endIndex < text.length())
						{
							super.send(text.substring(currentIndex, endIndex));
							currentIndex = endIndex;
						}
						super.sendStyle(HighlightStringStyle.createEndStyle(highlightEndIndexes.get(endIndex)));
						iter.remove();
					}
				}
				super.send(text.substring(currentIndex, text.length()));
			}
			
			for (HighlightString string : fullLineStrings)
			{
				super.sendStyle(HighlightStringStyle.createEndStyle(string));
			}
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
