/*
 * Created on Jan 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.stormfront.internal.StormFrontStyle;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

/**
 * @author Marshall
 *
 * Handles Style nodes. This is a basically a no-op handler so that we can handle styled text for now, and apply style later.
 */
public class StyleTagHandler extends DefaultTagHandler {

	private String currentStyleId;
	private StormFrontStyle currentStyle;
	
	/**
	 * @param handler
	 */
	public StyleTagHandler(IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String getName() {
		return "style";
	}

	public void handleStart(Attributes atts) {
		String styleId = atts.getValue("id");
		
		if (styleId == null || styleId.length() == 0)
		{
			StringBuffer buffer = DocumentTagHandler.getBuffer(currentStyleId);
			handler.getClient().append(IWarlockClient.DEFAULT_VIEW, buffer.toString(), currentStyle);
			
			DocumentTagHandler.stopCollecting(currentStyleId);
		}
		else
		{
			DocumentTagHandler.startCollecting(styleId);
			currentStyle = StormFrontStyle.createCustomStyle(styleId);
		}

		currentStyleId = styleId;
	}
}
