package cc.warlock.stormfront.internal;

import java.util.Hashtable;

import cc.warlock.client.internal.WarlockStyle;
import cc.warlock.stormfront.IStormFrontProtocolHandler;


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
	public void handleStart(Hashtable<String,String> atts) {
		this.id = atts.get("id");
		handler.setCurrentStyle(WarlockStyle.createCustomStyle(id, 0, -1));
	}
	
	@Override
	public void handleEnd() {
		handler.clearCurrentStyle();
	}

}
