/*
 * Created on Jan 15, 2005
 */
package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.BarStatus;
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
    	String id = attributes.getValue("id");
    	int value = Integer.parseInt(attributes.getValue("value"));
    	String text = attributes.getValue("text");
    	BarStatus bar = new BarStatus(value, text);

		IStormFrontClient client = handler.getClient();
		
		if (id.equals("health"))
		{
			client.getHealth().set(bar);
		}
		else if (id.equals("mana"))
		{
			client.getMana().set(bar);
		}
		else if (id.equals("stamina"))
		{
			client.getFatigue().set(bar);
		}
		else if (id.equals("spirit"))
		{
			client.getSpirit().set(bar);
		}
	}
}
