package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class PlayerIDTagHandler extends DefaultTagHandler {

	public PlayerIDTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "playerID" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		handler.getClient().getPlayerId().set(attributes.getValue("id"));
	}

}
