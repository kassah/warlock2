package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

public class IndicatorTagHandler extends DefaultTagHandler {

	public IndicatorTagHandler (IStormFrontProtocolHandler handler)
	{
		super(handler);
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "indicator" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes) {
		if (attributes.getAttribute("id") != null && attributes.getAttribute("visible") != null)
		{
			if ("y".equalsIgnoreCase(attributes.getValue("visible")))
			{
				handler.getClient().getCharacterStatus().set(attributes.getValue("id"));
			}
			else {
				handler.getClient().getCharacterStatus().unset(attributes.getValue("id"));
			}
		}
	}

}
