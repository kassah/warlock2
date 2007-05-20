/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

/**
 * @author Sean Proctor
 */
public class PromptTagHandler extends DefaultTagHandler {
	
	protected int currentTime = 0;
	protected RoundtimeTagHandler roundtimeHandler;
	protected IStormFrontClient client;
	protected String prompt;
	
	public PromptTagHandler (IStormFrontProtocolHandler handler, RoundtimeTagHandler roundtimeHandler) {
		super(handler);
		this.roundtimeHandler = roundtimeHandler;
		client = handler.getClient();
	}
	
	public String[] getTagNames() {
		return new String[] { "prompt" };
	}
	
	public void handleStart(Attributes atts) {
		prompt = null;
		
		if (atts.getValue("time") != null)
		{
			currentTime = Integer.parseInt(atts.getValue("time"));
			if (roundtimeHandler.isWaitingForPrompt()) {
				roundtimeHandler.processFollowingPrompt(currentTime);
			}
			
			prompt = "";
		}
	}
	
	public boolean handleCharacters(char[] ch, int start, int length) {
		prompt += String.copyValueOf(ch, start, length);
		return true;
	}
	
	@Override
	public void handleEnd() {
		if (prompt != null)
			client.getDefaultStream().prompt(prompt);
	}
}
