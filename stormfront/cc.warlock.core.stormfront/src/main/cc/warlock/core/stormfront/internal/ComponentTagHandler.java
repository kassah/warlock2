package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.internal.StormFrontClient;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class ComponentTagHandler extends DefaultTagHandler {

	protected String id;
	protected StringBuffer componentText = new StringBuffer();
	
	public ComponentTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		
		addTagHandler(new BTagHandler(handler));
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "component" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes) {
		id = attributes.getValue("id");
		componentText.setLength(0);
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		componentText.append(characters);
		
		return true;
	}
	
	@Override
	public void handleEnd() {
		if (id != null) {
			((StormFrontClient)handler.getClient()).setComponent(id, componentText.toString());
		}
	}

}
