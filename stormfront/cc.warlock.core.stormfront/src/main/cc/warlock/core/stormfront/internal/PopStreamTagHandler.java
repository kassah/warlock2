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

	@Override
	public String[] getTagNames() {
		return new String[] { "popStream" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes) {
		handler.popStream();
	}
	
	@Override
	public boolean ignoreNewlines() {
		return false;
	}
}
