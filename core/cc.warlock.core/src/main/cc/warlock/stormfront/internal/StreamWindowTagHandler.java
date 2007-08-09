package cc.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import cc.warlock.stormfront.IStormFrontProtocolHandler;


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
	public void handleStart(Attributes attributes) {
		String title = attributes.getValue("title");
		String id = attributes.getValue("id");
		
		if (id != null && title != null)
		{
			handler.getClient().getStream(id).getTitle().set(title);
		}
	}
}