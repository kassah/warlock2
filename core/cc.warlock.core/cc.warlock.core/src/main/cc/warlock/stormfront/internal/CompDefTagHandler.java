/*
 * Created on Jan 15, 2005
 */
package cc.warlock.stormfront.internal;

import java.util.HashMap;
import java.util.Map;

import cc.warlock.stormfront.IStormFrontProtocolHandler;
import cc.warlock.stormfront.IStormFrontTagHandler;


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
	Map<String, IStormFrontTagHandler> tagHandlers = new HashMap<String, IStormFrontTagHandler>();
	
	public CompDefTagHandler (IStormFrontProtocolHandler handler) {
		super(handler);
		
		tagHandlers.put("d", new DirectionTagHandler(handler));
	}
	
	public String[] getTagNames() {
		return new String[] { "compDef" };
	}

	public Map<String, IStormFrontTagHandler> getTagHandlers() {
		return tagHandlers;
	}
	
	public void handleStart(Map<String,String> attributes) {
		if (attributes.get("id").equals("room exits")) {
			handler.getClient().getCompass().clear();					
		}
	}
}

