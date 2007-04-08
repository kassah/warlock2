/*
 * Created on Jan 16, 2005
 */
package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

/**
 * @author Sean Proctor
 * 
 * A handler for pushStream elements
 */
public class PushStreamTagHandler extends DefaultTagHandler {

	public PushStreamTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}

	public String[] getTagNames() {
		return new String[] { "pushStream" };
	}
	
	public void handleStart(Attributes atts) {
		String id = atts.getValue("id");
	}
}
