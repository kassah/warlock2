/*
 * Created on Jan 15, 2005
 */
package cc.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import cc.warlock.stormfront.IStormFrontProtocolHandler;


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
	
	public CompDefTagHandler (IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "compDef" };
	}

	public void handleStart(Attributes atts) {
		if (atts.getValue("id").equals("room exits")) {
			handler.getClient().getCompass().clear();					
		}
	}
}
