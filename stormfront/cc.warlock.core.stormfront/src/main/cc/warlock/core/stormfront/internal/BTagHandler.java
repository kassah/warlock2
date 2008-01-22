package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


public class BTagHandler extends DefaultTagHandler {

	public BTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "b" };
	}
	
	@Override
	public boolean ignoreNewlines() {
		return false;
	}

}
