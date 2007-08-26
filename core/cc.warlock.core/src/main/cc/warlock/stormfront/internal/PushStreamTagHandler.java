/*
 * Created on Jan 16, 2005
 */
package cc.warlock.stormfront.internal;

import java.util.Hashtable;

import cc.warlock.stormfront.IStormFrontProtocolHandler;


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
	
	public void handleStart(Hashtable<String,String> attributes) {
		String id = attributes.get("id");
		handler.pushStream(id);
	}
}
