package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class StreamWindowTagHandler extends DefaultTagHandler {

	public StreamWindowTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "streamWindow" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes) {
		String subtitle = attributes.getValue("subtitle");
		String title = attributes.getValue("title");
		String id = attributes.getValue("id");
		
		if (id != null && title != null)
		{
			if (subtitle != null)
				title += subtitle;
			
			handler.getClient().getStream(id).getTitle().set(title);
		}
	}
}
