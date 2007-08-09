package cc.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import cc.warlock.stormfront.IStormFrontProtocolHandler;


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
	
	public void handleStart(Attributes atts) {
		handler.popStream();
	}
}
