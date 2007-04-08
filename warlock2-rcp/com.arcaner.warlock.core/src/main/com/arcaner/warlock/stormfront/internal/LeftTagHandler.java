package com.arcaner.warlock.stormfront.internal;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class LeftTagHandler extends DefaultTagHandler {

	public LeftTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "left" };
	}

	public boolean handleCharacters(char[] ch, int start, int length) {
		return true;
	}
}
