package cc.warlock.core.stormfront.internal;

import java.net.MalformedURLException;
import java.net.URL;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.IStormFrontClientViewer;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class LaunchURLTagHandler extends DefaultTagHandler {

	protected String url;
	
	public LaunchURLTagHandler (IStormFrontProtocolHandler handler)
	{
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "LaunchURL" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes) {
		this.url = "http://www.play.net" + attributes.getValue("src");
	}
	
	@Override
	public void handleEnd() {
		for (IWarlockClientViewer viewer : handler.getClient().getViewers())
		{
			if (viewer instanceof IStormFrontClientViewer)
			{
				try {
					((IStormFrontClientViewer)viewer).launchURL(new URL(url));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
