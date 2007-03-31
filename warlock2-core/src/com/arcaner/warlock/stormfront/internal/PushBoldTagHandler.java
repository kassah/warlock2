package com.arcaner.warlock.stormfront.internal;

import com.arcaner.warlock.client.stormfront.internal.StormFrontStyle;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class PushBoldTagHandler extends DefaultTagHandler {

	public PushBoldTagHandler (IStormFrontProtocolHandler handler)
	{
		super(handler);
	}
	
	@Override
	public String getName() {
		return "pushBold";
	}
	
	public void handleEnd() {
		handler.getClient().setCurrentStyle(StormFrontStyle.BOLD_STYLE);
		DocumentTagHandler.startCollecting("bold");
	}
}
