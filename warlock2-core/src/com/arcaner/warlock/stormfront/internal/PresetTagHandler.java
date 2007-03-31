package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.stormfront.internal.StormFrontStyle;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class PresetTagHandler extends DefaultTagHandler {

	private String id;
	
	public PresetTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String getName() {
		return "preset";
	}
	
	@Override
	public void handleStart(Attributes atts) {
		this.id = atts.getValue("id");
	}
	
	@Override
	public boolean handleCharacters(char[] ch, int start, int length) {
		String str = new String(ch);
		String toAppend = str.substring(start, start+length);
		
		handler.getClient().append(IWarlockClient.DEFAULT_VIEW, toAppend, StormFrontStyle.createCustomStyle(id));
		return true;
	}
	
	@Override
	public void handleEnd() {
		// TODO Auto-generated method stub
		super.handleEnd();
	}

}
