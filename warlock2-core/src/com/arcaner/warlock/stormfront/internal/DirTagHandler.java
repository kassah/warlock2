package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

/**
 * 
 * @author Marshall
 * @deprecated 
 */
public class DirTagHandler extends DefaultTagHandler {

	public DirTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String getName() {
		return "dir";
	}

	public void handleStart(Attributes atts) {
    	for (int i = 0; i < atts.getLength(); i++) {
			if(atts.getLocalName(i) == "value" || atts.getQName(i) == "value") {
				handler.getClient().getCompass().set(atts.getValue(i));
			}
    	}
	}
}
