/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

/**
 * @author Marshall
 * 
 * An XPath listener that handles the compass.
 * @deprecated  The Compass tag is more for highlighting and not
 * for the compass. The server sends a compass tag for peers, and other things.
 */
public class CompassTagHandler extends DefaultTagHandler {
	
	public CompassTagHandler (IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String getName() {
		return "compass";
	}

	public void handleStart(Attributes atts) {
		handler.getClient().getCompass().clear();
	}

}
