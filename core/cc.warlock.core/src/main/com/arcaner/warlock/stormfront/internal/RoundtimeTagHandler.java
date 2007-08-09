/*
 * Created on Jan 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RoundtimeTagHandler extends DefaultTagHandler {
	protected boolean waitingForPrompt, roundtimeStarted;
	protected int rtEnds;
	
	public boolean isWaitingForPrompt() { return waitingForPrompt; }
	
	public RoundtimeTagHandler (IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "roundTime" };
	}
	
	public void handleStart(Attributes atts) {
		rtEnds = Integer.parseInt(atts.getValue("value"));
		waitingForPrompt = true;
		roundtimeStarted = false;
	}

	public void handleEnd() {
		// TODO investigate whether or not we need to do something here
	}
	
	public void processFollowingPrompt (int currentPromptTime)
	{
		int time = rtEnds - currentPromptTime;
		handler.getClient().startRoundtime(time);
		
		waitingForPrompt = false;
	}
}
