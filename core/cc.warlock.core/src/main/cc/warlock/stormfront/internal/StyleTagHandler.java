/*
 * Created on Jan 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import cc.warlock.client.IWarlockStyle;
import cc.warlock.client.internal.WarlockStyle;
import cc.warlock.stormfront.IStormFrontProtocolHandler;


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

	public void handleStart(Attributes atts) {
		String styleId = null;
		
		if ("style".equals(getCurrentTag()))
			styleId = atts.getValue("id");
		else if ("output".equals(getCurrentTag()))
			styleId = atts.getValue("class");
		
		if (styleId == null || styleId.length() == 0)
		{
			currentStyle.setLength(handler.peekBuffer().getBuffer().length());
			handler.peekBuffer().addStyle(currentStyle, 0);
			
			handler.sendAndPopBuffer();
			handler.clearCurrentStyle();
		}
		else
		{
			handler.pushBuffer();
			currentStyle = WarlockStyle.createCustomStyle(styleId, 0, -1);
			
			if (styleId.equals("mono")) {
				currentStyle.addStyleType(IWarlockStyle.StyleType.MONOSPACE);
			}
			handler.setCurrentStyle(currentStyle);
			
		}
	}
}
