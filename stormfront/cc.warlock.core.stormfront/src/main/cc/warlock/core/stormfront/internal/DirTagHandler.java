package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class DirTagHandler extends DefaultTagHandler {
	
	CompassTagHandler compass;
	
	public DirTagHandler(IStormFrontProtocolHandler handler, CompassTagHandler compass) {
		super(handler);
		this.compass = compass;
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "dir" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes) {
		String dir = attributes.getValue("value");
		if(dir != null)
			compass.addDirection(dir);
	}
}

