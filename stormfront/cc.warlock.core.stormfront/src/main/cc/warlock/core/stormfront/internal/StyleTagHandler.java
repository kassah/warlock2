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
import cc.warlock.core.stormfront.serversettings.server.Preset;
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
		return new String[] { "style" };
	}

	public void handleStart(StormFrontAttributeList attributes) {
		if(currentStyle != null) {
			handler.getCurrentStream().removeStyle(currentStyle);
			currentStyle = null;
		}
		
		String styleId = attributes.getValue("id");
		
		if (styleId != null && styleId.length() > 0)
		{
			Preset preset = handler.getClient().getServerSettings().getPreset(styleId);
			if(preset != null)
				currentStyle = preset.getStyle();
			else
				currentStyle = new WarlockStyle();
			
			handler.getCurrentStream().addStyle(currentStyle);
		}
	}
}
