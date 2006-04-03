package com.arcaner.warlock.stormfront.internal;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class SpellTagHandler extends DefaultTagHandler {

	public SpellTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String getName() {
		return "spell";
	}

	public boolean handleCharacters(char[] ch, int start, int length) {
		return true;
	}
}
