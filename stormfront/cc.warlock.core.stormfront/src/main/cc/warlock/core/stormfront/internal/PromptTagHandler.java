/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


/**
 * @author Sean Proctor
 */
public class PromptTagHandler extends DefaultTagHandler {
	
	protected long currentTime = 0;
	protected RoundtimeTagHandler roundtimeHandler;
	protected IStormFrontClient client;
	protected StringBuffer prompt = new StringBuffer();
	
	public PromptTagHandler (IStormFrontProtocolHandler handler, RoundtimeTagHandler roundtimeHandler) {
		super(handler);
		this.roundtimeHandler = roundtimeHandler;
		client = handler.getClient();
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "prompt" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		//System.out.println("got prompt");
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
	public void handleEnd(String newLine) {
		client.getDefaultStream().prompt(prompt.toString());
	}
}
