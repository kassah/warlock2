package cc.warlock.core.stormfront.internal;

import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


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
	public void handleStart(StormFrontAttributeList attributes) {
		this.id = attributes.getValue("id");
		
		handler.getCurrentStream().sendStyle(WarlockStyle.createCustomStyle(id));
	}
	
	@Override
	public void handleEnd() {
		handler.getCurrentStream().sendStyle(WarlockStyle.createEndCustomStyle(id));
	}

}
