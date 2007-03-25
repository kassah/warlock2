package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class InvTagHandler extends DefaultTagHandler {

	public InvTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String getName() {
		return "inv";
	}

	public void handleStart(Attributes atts) {
	}
	
	public boolean handleCharacters(char[] ch, int start, int length) {
		return true;
	}
}
