package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;

public class DTagHandler extends DefaultTagHandler {

	public DTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] {"d"};
	}

	@Override
	public boolean ignoreNewlines() {
		return false;
	}
}
