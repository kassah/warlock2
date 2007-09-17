package cc.warlock.core.stormfront.internal;

import java.util.Map;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


public class ClearStreamTagHandler extends DefaultTagHandler {
	
	/**
	 * @param handler
	 */
	public ClearStreamTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}

	public String[] getTagNames() {
		return new String[] { "clearStream"};
	}
	
	public void handleStart(Map<String,String> attributes) {
		String id = attributes.get("id");
		
		handler.getClient().getStream(id).clear();
	}
}
