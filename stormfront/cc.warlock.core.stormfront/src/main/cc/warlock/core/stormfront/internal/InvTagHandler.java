package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


public class InvTagHandler extends DefaultTagHandler {

	public InvTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "inv" };
	}

	public boolean handleCharacters(char[] ch, int start, int length) {
		return true;
	}
}
