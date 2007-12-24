/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


/**
 * @author Sean Proctor
 */
public class PromptTagHandler extends DefaultTagHandler {
	
	protected long currentTime = 0;
	protected RoundtimeTagHandler roundtimeHandler;
	protected StringBuffer prompt = new StringBuffer();
	protected boolean waitingForInitialStreams = false;
	
	public PromptTagHandler (IStormFrontProtocolHandler handler, RoundtimeTagHandler roundtimeHandler) {
		super(handler);
		this.roundtimeHandler = roundtimeHandler;
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "prompt" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes) {
		handler.clearStyles();
		prompt.setLength(0);
		
		if (attributes.getValue("time") != null)
		{
			currentTime = Long.parseLong(attributes.getValue("time"));
			if (roundtimeHandler.isWaitingForPrompt()) {
				roundtimeHandler.processFollowingPrompt(currentTime);
			}
		}
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		prompt.append(characters);
		return true;
	}
	
	@Override
	public void handleEnd() {
		handler.getClient().getDefaultStream().prompt(prompt.toString());
		
		if (waitingForInitialStreams)
		{
			handler.getClient().getServerSettings().sendInitialStreamWindows();
			waitingForInitialStreams = false;
		}
	}

	public boolean isWaitingForInitialStreams() {
		return waitingForInitialStreams;
	}

	public void setWaitingForInitialStreams(boolean waitingForInitialStreams) {
		this.waitingForInitialStreams = waitingForInitialStreams;
	}
}
