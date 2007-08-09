package cc.warlock.stormfront.internal;

import org.xml.sax.Attributes;

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
	
	public void handleStart(Attributes atts) {
		String id = atts.getValue("id");
		
		handler.getClient().getStream(id).clear();
	}
}
