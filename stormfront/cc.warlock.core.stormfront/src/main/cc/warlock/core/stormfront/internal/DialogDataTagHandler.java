package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;

public class DialogDataTagHandler extends DefaultTagHandler {

	DialogDataTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		
		addTagHandler(new BarTagHandler(handler));
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "dialogData" };
	}

}
