/**
 * 
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

/**
 * @author kassah
 *
 */
public class ClearContainerTagHandler extends DefaultTagHandler {

	/**
	 * @param handler
	 */
	public ClearContainerTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}

	@Override
	public String[] getTagNames() {
		return new String[] { "clearContainer"};
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		String id = attributes.getValue("id") + "Container";
		handler.getClient().getStream(id).clear();
	}
}
