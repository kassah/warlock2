package cc.warlock.stormfront.internal;

import java.util.Map;

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
	public void handleStart(Map<String,String> attributes) {
		String title = attributes.get("title");
		String id = attributes.get("id");
		
		if (id != null && title != null)
		{
			handler.getClient().getStream(id).getTitle().set(title);
		}
	}
}
