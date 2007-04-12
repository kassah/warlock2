package com.arcaner.warlock.stormfront.internal;

import com.arcaner.warlock.client.internal.WarlockStyle;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class PushBoldTagHandler extends DefaultTagHandler {

	public PushBoldTagHandler (IStormFrontProtocolHandler handler)
	{
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "pushBold" };
	}
	
	public void handleEnd() {
		handler.setCurrentStyle(WarlockStyle.BOLD_STYLE);
//		handler.pushBuffer();
	}
}
