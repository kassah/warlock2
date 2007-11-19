package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


public class DirectionTagHandler extends DefaultTagHandler {
	
	public DirectionTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "d" };
	}

	@Override
	public boolean handleCharacters(char[] ch, int start, int length) {
		handler.getClient().getCompass().set(String.copyValueOf(ch, start, length));
		
		return false;
	}
}

