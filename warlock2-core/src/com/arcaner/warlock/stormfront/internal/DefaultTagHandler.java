package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;
import com.arcaner.warlock.stormfront.IStormFrontTagHandler;

abstract public class DefaultTagHandler implements IStormFrontTagHandler {
	protected IStormFrontProtocolHandler handler;
	
	public DefaultTagHandler(IStormFrontProtocolHandler handler) {
		this.handler = handler;
		handler.registerHandler(this);
	}
	
	public abstract String[] getTagNames();
	
	public void handleStart(Attributes atts) {
		// TODO Auto-generated method stub

	}

	public void handleEnd() {
		// TODO Auto-generated method stub

	}

	public boolean handleCharacters(char[] ch, int start, int length) {
		return false;
	}
}
