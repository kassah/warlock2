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
	
	protected StringBuffer prompt = new StringBuffer();
	protected boolean waitingForInitialStreams = false;
	
	public PromptTagHandler (IStormFrontProtocolHandler handler) {
		super(handler);
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
			handler.getClient().setTime(Long.parseLong(attributes.getValue("time")));
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
