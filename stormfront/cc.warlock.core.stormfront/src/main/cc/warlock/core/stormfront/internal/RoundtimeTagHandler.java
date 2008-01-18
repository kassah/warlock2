/*
 * Created on Jan 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RoundtimeTagHandler extends DefaultTagHandler {
	
	public RoundtimeTagHandler (IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "roundTime" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes) {
		long rtEnd = Long.parseLong(attributes.getValue("value"));
		long rtLength = rtEnd - handler.getClient().getTime();
		/* sometimes we're poorly synced and end up with a RT < 1,
		 * so make the value 1 for that case */
		if(rtLength < 1)
			rtLength = 1;
		handler.getClient().startRoundtime((int)rtLength);
	}

}
