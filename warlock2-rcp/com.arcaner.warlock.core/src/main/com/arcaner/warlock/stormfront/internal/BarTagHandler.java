/*
 * Created on Jan 15, 2005
 */
package com.arcaner.warlock.stormfront.internal;

import org.xml.sax.Attributes;

import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.stormfront.IStormFrontProtocolHandler;

/**
 * @author Marshall
 *
 * An XPath Listener that handles the health, mana, fatigue, and spirit bars.
 */
public class BarTagHandler extends DefaultTagHandler {
	
	public BarTagHandler (IStormFrontProtocolHandler handler) {
		super(handler);
	}
	
	public String[] getTagNames() {
		return new String[] { "progressBar" };
	}

	public void handleStart(Attributes atts) {
    	handleProgressBar(atts.getValue("id"), Integer.parseInt(atts.getValue("value")), atts.getValue("text"));	
	}
	
	private void handleProgressBar (String which, int percentage, String label)
	{
		IStormFrontClient client = handler.getClient();
		
		if (which.equals("health"))
		{
			client.getHealth().set(percentage);
		}
		else if (which.equals("mana"))
		{
			client.getMana().set(percentage);
		}
		else if (which.equals("stamina"))
		{
			client.getFatigue().set(percentage);
		}
		else if (which.equals("spirit"))
		{
			client.getSpirit().set(percentage);
		}
	}

}
