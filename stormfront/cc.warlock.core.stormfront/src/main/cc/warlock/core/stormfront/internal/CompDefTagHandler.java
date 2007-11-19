/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.internal.StormFrontClient;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


/**
 * @author Rob
 * 
 * An XPath listener that handles compDef, which 
 * puts forth information about the room you just entered 
 * and its exits.
 * THis is only activated if you ENTER the room. Not 
 * looking, or peering.
 */
public class CompDefTagHandler extends DefaultTagHandler {
	
	protected String id;
	protected StringBuffer buffer = new StringBuffer();
	
	public CompDefTagHandler (IStormFrontProtocolHandler handler) {
		super(handler);
		
		addTagHandler("d", new DirectionTagHandler(handler));
	}
	
	public String[] getTagNames() {
		return new String[] { "compDef" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		buffer.setLength(0);
		this.id = attributes.getValue("id");
		
		if (attributes.getValue("id").equals("room exits")) {
			handler.getClient().getCompass().clear();
		}
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		if (id != null && id.equals("room desc")) {
			buffer.append(characters);
		}
		// let the stream have the text, we just want to store the value in a property
		return false;
	}
	
	@Override
	public void handleEnd(String newLine) {
		if (id != null && id.equals("room desc"))
		{
			handler.getClient().getRoomDescription().set(buffer.toString());
		}
		handler.getCurrentStream().send("\n");

		StormFrontClient client = (StormFrontClient) handler.getClient();
		client.setComponent(id, buffer.toString());
	}
}

