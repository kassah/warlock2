package cc.warlock.stormfront.internal;

import java.util.Hashtable;

import cc.warlock.stormfront.IStormFrontProtocolHandler;


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
	public void handleStart(Hashtable<String,String> attributes) {
		handler.getClient().getPlayerId().set(attributes.get("id"));
	}

}
