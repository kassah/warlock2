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

	@Override
	public String[] getTagNames() {
		return new String[] { "pushStream" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		String id = attributes.getValue("id");
		
		String closedStyle = attributes.getValue("ifClosedStyle");
		boolean watch = false;
		if(closedStyle != null && closedStyle.equals("watching"))
			watch = true;
		
		handler.pushStream(id, watch);
		if(newLine != null && newLine.length() > 0) {
			handler.characters(newLine);
		}
	}
	
	@Override
	public void handleEnd(String newLine) {
		if(newLine != null && newLine.length() > 0) {
			handler.characters(newLine);
		}
	}
}
