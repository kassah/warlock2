package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


public class ComponentTagHandler extends DefaultTagHandler {

	public ComponentTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "component" };
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		return true;
	}

}
