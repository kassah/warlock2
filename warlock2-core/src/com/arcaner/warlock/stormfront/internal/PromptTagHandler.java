/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

/**
 * @author Sean Proctor
 */
public class PromptTagHandler extends DefaultTagHandler {
	
	protected int lastTime = 0;
	protected RoundtimeTagHandler roundtimeHandler;
	protected IStormFrontClient client;
	
	public PromptTagHandler (IStormFrontProtocolHandler handler, RoundtimeTagHandler roundtimeHandler) {
		super(handler);
		this.roundtimeHandler = roundtimeHandler;
		client = handler.getClient();
	}
	
	public String getName() {
		return "prompt";
	}
	
	public void handleStart(Attributes atts) {
		int time = Integer.parseInt(atts.getValue("time"));
		if (roundtimeHandler.isWaitingForPrompt()) {
			roundtimeHandler.processFollowingPrompt(time);
		}
		
//		if (lastTime != time)
//		{
//			handler.getClient().append();
//			lastTime = time;
//		}
	}

	public void handleEnd() {
		client.setPrompting();
	}
	
	public boolean handleCharacters(char[] ch, int start, int length) {
		if(!client.isPrompting()) {
			String str = String.copyValueOf(ch, start, length);
			client.append(IWarlockClient.DEFAULT_VIEW, str);
		}
		return true;
	}
}
