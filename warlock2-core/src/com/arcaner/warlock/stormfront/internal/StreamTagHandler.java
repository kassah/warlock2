package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class StreamTagHandler extends DefaultTagHandler {

	public StreamTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}

	@Override
	public String getName() {
		return "stream";
	}

	 @Override
	public void handleStart(Attributes attributes) {
		String streamId = new String(attributes.getValue("id"));
		
		handler.pushStream(streamId);
	}
}
