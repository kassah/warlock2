package cc.warlock.rcp.ui.style;

import java.util.Hashtable;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.rcp.ui.IStyleProvider;

public class StyleProviders {

	public static Hashtable<IWarlockClient, IStyleProvider> styleProviders = new Hashtable<IWarlockClient, IStyleProvider>();
	
	public static IStyleProvider getStyleProvider (IWarlockClient client)
	{
		if (styleProviders.containsKey(client))
			return styleProviders.get(client);
		else return null;
	}
	
	public static void setStyleProvider (IWarlockClient client, IStyleProvider provider)
	{
		styleProviders.put(client, provider);
	}
}
