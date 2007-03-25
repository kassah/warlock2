package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class DirectionTagHandler extends DefaultTagHandler {
	
	public DirectionTagHandler (IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String getName() {
		return "d";
	}

	public void handleStart(Attributes atts) {
	}
	
	public boolean handleCharacters(char[] ch, int start, int length) {
		String dir = new String(ch, start, length);
		//System.out.println("dir is: " + dir);
		try {
			handler.getClient().getCompass().set(dir);
		} catch( Exception e ) {
			System.out.println("THERE'S AN ERROR HERE!!! DirectionTagHandler.java");
		}
		return false;
	}
}

