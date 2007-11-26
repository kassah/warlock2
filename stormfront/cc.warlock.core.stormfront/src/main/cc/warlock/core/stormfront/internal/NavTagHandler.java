package cc.warlock.core.stormfront.internal;

import cc.warlock.core.script.IScript;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


public class NavTagHandler extends DefaultTagHandler {

	public NavTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "nav" };
	}
	
	@Override
	public void handleEnd(String newLine) {
		for (IScript script : handler.getClient().getRunningScripts())
		{
			script.movedToRoom();
		}
	}

}
