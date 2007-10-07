package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class PopStreamTagHandler extends DefaultTagHandler {

	/**
	 * @param handler
	 */
	public PopStreamTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}

	public String[] getTagNames() {
		return new String[] { "popStream" };
	}
	
	public void handleStart(StormFrontAttributeList attributes) {
		handler.popStream();
	}
}
