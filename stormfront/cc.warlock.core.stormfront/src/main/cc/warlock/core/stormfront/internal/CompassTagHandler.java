package cc.warlock.core.stormfront.internal;

import cc.warlock.core.script.IScript;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;


public class CompassTagHandler extends DefaultTagHandler {

	public CompassTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "compass" };
	}
	
	@Override
	public void handleEnd(String newLine) {
		for (IScript script : handler.getClient().getRunningScripts())
		{
			script.movedToRoom();
		}
	}

}
