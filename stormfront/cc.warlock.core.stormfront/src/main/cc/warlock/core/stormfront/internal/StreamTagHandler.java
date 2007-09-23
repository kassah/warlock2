package cc.warlock.core.stormfront.internal;

import java.util.Map;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


public class StreamTagHandler extends DefaultTagHandler {

	public StreamTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);

		addTagHandler("preset", new PresetTagHandler(handler));
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "stream" };
	}

	 @Override
	public void handleStart(Map<String,String> attributes) {
		 String id = attributes.get("id");
		 if (id != null)
		 {
			String streamId = id;
			
			handler.pushStream(streamId);
		 }
	}
	 
	 @Override
	public void handleEnd() {
		 // force append a new-line.. most of the use of <stream>xxx</stream> doesn't have newlines, so the buffer won't flush
		 handler.characters(new char[] { '\n' }, 0, 1);
		 handler.popStream();
	}
}
