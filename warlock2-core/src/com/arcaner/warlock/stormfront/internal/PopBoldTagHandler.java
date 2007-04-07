package com.arcaner.warlock.stormfront.internal;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.stormfront.internal.StormFrontStyle;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

public class PopBoldTagHandler extends DefaultTagHandler {

	public PopBoldTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}

	@Override
	public String getName() {
		return "popBold";
	}
	
	@Override
	public boolean handleCharacters(char[] ch, int start, int length) {
		return true;
	}
	
	@Override
	public void handleEnd() {
		StringBuffer boldText = handler.popBuffer();
		
		handler.getClient().append(IWarlockClient.DEFAULT_VIEW, boldText.toString(), StormFrontStyle.BOLD_STYLE);
		handler.getClient().setCurrentStyle(StormFrontStyle.EMPTY_STYLE);
	}

}
