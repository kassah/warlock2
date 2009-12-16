/**
 * 
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;

/**
 * @author kassah
 *
 */
public class CasttimeTagHandler extends DefaultTagHandler {

	public CasttimeTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see cc.warlock.core.stormfront.internal.BaseTagHandler#getTagNames()
	 */
	@Override
	public String[] getTagNames() {
		return new String[] { "castTime" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String rawXML) {
		handler.getClient().setupCasttime(new Long(attributes.getValue("value")));
	}
}
