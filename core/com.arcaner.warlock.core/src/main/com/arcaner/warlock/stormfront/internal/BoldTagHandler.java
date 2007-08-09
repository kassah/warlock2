package com.arcaner.warlock.stormfront.internal;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class BoldTagHandler extends DefaultTagHandler {

	public BoldTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "b" };
	}
	
	@Override
	public boolean handleCharacters(char[] ch, int start, int length) {
		return true;
	}

}
