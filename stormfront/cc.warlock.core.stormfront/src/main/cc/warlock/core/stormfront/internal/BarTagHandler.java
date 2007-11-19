/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


/**
 * @author Marshall
 *
 * An XPath Listener that handles the health, mana, fatigue, and spirit bars.
 */
public class BarTagHandler extends BaseTagHandler {
	private IStormFrontProtocolHandler handler;
	
	public BarTagHandler (IStormFrontProtocolHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public String[] getTagNames() {
		return new String[] { "progressBar" };
	}

	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
    	handleProgressBar(attributes.getValue("id"), Integer.parseInt(attributes.getValue("value")), attributes.getValue("text"));	
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
