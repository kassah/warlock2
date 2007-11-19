/*
 * Created on Jan 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.client.IWarlockStyle;
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
	
	@Override
	public String[] getTagNames() {
		return new String[] { "style" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		if(currentStyle != null) {
			handler.removeStyle(currentStyle);
			currentStyle = null;
		}
		
		String styleId = attributes.getValue("id");
		
		if (styleId != null && styleId.length() > 0)
		{
			Preset preset = handler.getClient().getServerSettings().getPreset(styleId);
			if(preset != null) {
				currentStyle = preset.getStyle();
				handler.addStyle(currentStyle);
			}
		}
		
		if(newLine != null && newLine.length() > 0) {
			char[] chars = new char[newLine.length()];
			newLine.getChars(0, newLine.length(), chars, 0);
			handler.characters(chars, 0, newLine.length());
		}
	}
	
	@Override
	public void handleEnd(String newLine) {
		if(newLine != null && newLine.length() > 0) {
			char[] chars = new char[newLine.length()];
			newLine.getChars(0, newLine.length(), chars, 0);
			handler.characters(chars, 0, newLine.length());
		}
	}
}
