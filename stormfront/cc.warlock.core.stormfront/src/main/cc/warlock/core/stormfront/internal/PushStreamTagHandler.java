/*
 * Created on Jan 16, 2005
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


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
	
	public void handleStart(StormFrontAttributeList attributes) {
		String id = attributes.getValue("id");
		handler.pushStream(id);
	}
}
