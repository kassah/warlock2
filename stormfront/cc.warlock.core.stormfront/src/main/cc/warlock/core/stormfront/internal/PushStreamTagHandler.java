/*
 * Created on Jan 16, 2005
 */
package cc.warlock.core.stormfront.internal;

import java.util.Map;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


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
	
	public void handleStart(Map<String,String> attributes) {
		String id = attributes.get("id");
		handler.pushStream(id);
	}
}
