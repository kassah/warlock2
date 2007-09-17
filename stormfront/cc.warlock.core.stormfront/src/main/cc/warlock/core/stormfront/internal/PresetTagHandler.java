package cc.warlock.core.stormfront.internal;

import java.util.Map;

import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


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
	public void handleStart(Map<String,String> atts) {
		this.id = atts.get("id");
		handler.setCurrentStyle(WarlockStyle.createCustomStyle(id, 0, -1));
	}
	
	@Override
	public void handleEnd() {
		handler.clearCurrentStyle();
	}

}
