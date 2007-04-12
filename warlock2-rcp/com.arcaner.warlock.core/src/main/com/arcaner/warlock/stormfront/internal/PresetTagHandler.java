package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.internal.WarlockStyle;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class PresetTagHandler extends DefaultTagHandler {

	private String id;
	
	public PresetTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "preset" };
	}
	
	@Override
	public void handleStart(Attributes atts) {
		this.id = atts.getValue("id");
		handler.setCurrentStyle(WarlockStyle.createCustomStyle(id));
	}
	
	@Override
	public void handleEnd() {
		handler.clearCurrentStyle();
	}

}
