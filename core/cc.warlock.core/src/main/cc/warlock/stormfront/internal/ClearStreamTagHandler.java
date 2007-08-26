package cc.warlock.stormfront.internal;

import java.util.Hashtable;

import cc.warlock.stormfront.IStormFrontProtocolHandler;


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
	
	public void handleStart(Hashtable<String,String> attributes) {
		String id = attributes.get("id");
		
		handler.getClient().getStream(id).clear();
	}
}
