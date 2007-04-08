package com.arcaner.warlock.stormfront.internal;

import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class PopBoldTagHandler extends DefaultTagHandler {

	public PopBoldTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "popBold" };
	}
	
	@Override
	public void handleEnd() {
		handler.clearCurrentStyle();
//		StringBuffer boldText = handler.popBuffer();
//		
//		handler.getClient().append(IWarlockClient.DEFAULT_VIEW, boldText.toString(), StormFrontStyle.BOLD_STYLE);
//		handler.getClient().setCurrentStyle(StormFrontStyle.EMPTY_STYLE);
	}

}
