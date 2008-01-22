package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;

public class StubTagHandler extends DefaultTagHandler {

	public StubTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		addTagHandler(this); // recursive to handle stubs in stubs
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "skin", "forcesave", "exposeContainer", "container", "clearContainer", "openDialog",
				"menuLink", "menuImage", "image", "link", "sep", "switchQuickBar", "endSetup", };
	}

	@Override
	public boolean handleCharacters(String characters) {
		return true;
	}
}
