package com.arcaner.warlock.stormfront.internal;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

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
