package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


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
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		 String id = attributes.getValue("id");
		 if (id != null)
		 {
			String streamId = id;
			
			handler.pushStream(streamId, true);
		 }
	}
	 
	 @Override
	public void handleEnd(String newLine) {
		 // TODO flush the buffer here manually
		 // force append a new-line.. most of the use of <stream>xxx</stream> doesn't have newlines, so the buffer won't flush
		 handler.characters("\n");
		 handler.popStream();
	}
}
