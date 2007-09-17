package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


public class ComponentTagHandler extends DefaultTagHandler {

	public ComponentTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "component" };
	}
	
	public boolean handleCharacters(char[] ch, int start, int end) {
		return true;
	}

}
