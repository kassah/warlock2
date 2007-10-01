package cc.warlock.rcp.stormfront;

import org.eclipse.core.expressions.PropertyTester;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.rcp.ui.client.WarlockClientAdaptable;

public class StormFrontClientPropertyTester extends PropertyTester {

	public StormFrontClientPropertyTester() {
		// TODO Auto-generated constructor stub
	}

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		WarlockClientAdaptable adaptable = (WarlockClientAdaptable) receiver;
		IWarlockClient client = (IWarlockClient) adaptable.getAdapter(IWarlockClient.class);
		
		if ("clientConnected".equals(property))
		{
			boolean connected = client.getConnection() == null ? false : client.getConnection().isConnected();
			
			return expectedValue == null ?
				connected : connected == ((Boolean)expectedValue).booleanValue();
		}
		else if ("settingsLoaded".equals(property))
		{
		}
		
		return false;
	}

}
