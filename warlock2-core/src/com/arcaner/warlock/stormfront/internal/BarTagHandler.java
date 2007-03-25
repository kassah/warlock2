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
	
	public String getName() {
		return "progressBar";
	}
	
	/*public String[] getMatchableXPaths()
	{
		return new String[] { "//dialogData[@id='minivitals']/progressBar" };
	}*/

	public void handleStart(Attributes atts) {
    	handleProgressBar(
    	atts.getValue("id"), 
		Integer.parseInt(atts.getValue("value")),
		atts.getValue("text"));	

	}
	
	public void handleEnd() {
		// TODO Auto-generated method stub

	}
	
//	public void handleNodes(Node[] nodes, String matchedXPath)
//	{
//		super.handleNodes(nodes, matchedXPath);
//    	handleProgressBar(
//    		nodes[0].valueOf("@id"), 
//    		Integer.parseInt(nodes[0].valueOf("@value")),
//    		nodes[0].valueOf("@text"));	
//	}
//	
	private void handleProgressBar (String which, int percentage, String label)
	{
		IStormFrontClient client = handler.getClient();
		
		if (which.equals("health"))
		{
			client.setHealth(percentage, label);
		}
		else if (which.equals("mana"))
		{
			client.setMana(percentage, label);
		}
		else if (which.equals("stamina"))
		{
			client.setFatigue(percentage, label);
		}
		else if (which.equals("spirit"))
		{
			client.setSpirit(percentage, label);
		}
	}

}
