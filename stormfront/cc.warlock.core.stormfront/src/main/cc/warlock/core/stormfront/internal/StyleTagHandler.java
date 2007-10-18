/*
 * Created on Jan 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


/**
 * @author Marshall
 *
 * Handles Style nodes. This is a basically a no-op handler so that we can handle styled text for now, and apply style later.
 */
public class StyleTagHandler extends DefaultTagHandler {
	private IWarlockStyle currentStyle;
	
	/**
	 * @param handler
	 */
	public StyleTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "style", "output" };
	}

	public void handleStart(StormFrontAttributeList attributes) {
		String styleId = null;
		
		if ("style".equals(getCurrentTag()))
			styleId = attributes.getValue("id");
		else if ("output".equals(getCurrentTag()))
			styleId = attributes.getValue("class");
		
		if (styleId == null || styleId.length() == 0)
		{
			IWarlockStyle endStyle = WarlockStyle.createEndCustomStyle(currentStyle.getStyleName());
			handler.getCurrentStream().sendStyle(endStyle);
		}
		else
		{
			currentStyle = WarlockStyle.createCustomStyle(styleId);
			
			if (styleId.equals("mono")) {
				currentStyle.addStyleType(IWarlockStyle.StyleType.MONOSPACE);
			}
			
			handler.getCurrentStream().sendStyle(currentStyle);
		}
	}
}
