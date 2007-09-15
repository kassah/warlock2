package cc.warlock.stormfront.internal;

import java.util.Map;

import cc.warlock.stormfront.IStormFrontProtocolHandler;

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
	public void handleStart(Map<String, String> attributes) {
		if (attributes.get("id") != null && attributes.get("visible") != null)
		{
			if ("y".equalsIgnoreCase(attributes.get("visible")))
			{
				handler.getClient().getCharacterStatus().set(attributes.get("id"));
			}
			else {
				handler.getClient().getCharacterStatus().unset(attributes.get("id"));
			}
		}
	}

}
