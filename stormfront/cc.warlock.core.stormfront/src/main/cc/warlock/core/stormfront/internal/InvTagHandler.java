package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


public class InvTagHandler extends DefaultTagHandler {

	public InvTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "inv" };
	}

	@Override
	public boolean handleCharacters(String characters) {
		return true;
	}
}
