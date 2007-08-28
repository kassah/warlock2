package cc.warlock.stormfront.internal;

import cc.warlock.stormfront.IStormFrontProtocolHandler;

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
