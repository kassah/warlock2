package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class StreamTagHandler extends DefaultTagHandler {

	public StreamTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "stream" };
	}

	 @Override
	public void handleStart(Attributes attributes) {
		 String id = attributes.getValue("id");
		 if (id != null)
		 {
			String streamId = new String(id);
			
			handler.pushStream(streamId);
		 }
	}
	 
	 @Override
	public void handleEnd() {
		handler.popStream();
	}
}
