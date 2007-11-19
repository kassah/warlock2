/*
 * Created on Jan 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RoundtimeTagHandler extends DefaultTagHandler {
	protected boolean waitingForPrompt, roundtimeStarted;
	protected long rtEnds;
	
	public boolean isWaitingForPrompt() { return waitingForPrompt; }
	
	public RoundtimeTagHandler (IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "roundTime" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		rtEnds = Long.parseLong(attributes.getValue("value"));
		waitingForPrompt = true;
		roundtimeStarted = false;
	}
	
	public void processFollowingPrompt (long currentPromptTime)
	{
		long time = rtEnds - currentPromptTime;
		handler.getClient().startRoundtime((int) time);
		
		waitingForPrompt = false;
	}
}
