package cc.warlock.core.stormfront.client.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.warlock.core.client.WarlockString;
import cc.warlock.core.client.internal.Stream;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.serversettings.server.IgnoreSetting;

public class StormFrontStream extends Stream {
	
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
	public void send(WarlockString text) {
		if (client != null)
		{
			// Ignore text containing ignores
			for (IgnoreSetting ignore : ((IStormFrontClient)client).getServerSettings().getIgnores())
			{
				Pattern ignoreRegex = ignore.getRegex();
				int pos = 0;
				for(;;)
				{
					String str = text.toString();
					
					Matcher m = ignoreRegex.matcher(str);
					if(!m.find(pos))
						break;
					
					System.out.println("Ignore matched text in: " + text.toString());
					
					// remove text from beginning of line of match to end, find those points
					int start = str.substring(0, m.start()).lastIndexOf('\n');
					int end = str.indexOf('\n', m.end() - 1);
					
					// start index becomes index after the match once it's removed
					pos = start;
					
					// we are the first line and don't have anything to remove there
					if(start < 0) {
						// we only have one line, don't show anything
						if(end < 0 || end + 1 >= str.length())
							return;
						
						text = text.substring(end + 1);
					} else {
						WarlockString newText = text.substring(0, start + 1);
						
						// if we have text after the line after the ignore, add it
						if(end >= 0 && end + 1 < str.length())
							newText.append(text.substring(end + 1));

						text = newText;
					}
				}
			}
		}
		
		super.send(text);
	}
}
