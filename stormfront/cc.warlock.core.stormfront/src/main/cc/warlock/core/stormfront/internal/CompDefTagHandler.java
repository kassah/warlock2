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
		
		addTagHandler(new DTagHandler(handler));
		addTagHandler(new BTagHandler(handler));
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "compDef" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes) {
		buffer.setLength(0);
		this.id = attributes.getValue("id");
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		if (id != null) {
			buffer.append(characters);
		}
		// let the stream have the text, we just want to store the value in a property
		return false;
	}
	
	@Override
	public void handleEnd() {
		handler.getCurrentStream().send("\n");

		StormFrontClient client = (StormFrontClient) handler.getClient();
		client.setComponent(id, buffer.toString());
	}
}

