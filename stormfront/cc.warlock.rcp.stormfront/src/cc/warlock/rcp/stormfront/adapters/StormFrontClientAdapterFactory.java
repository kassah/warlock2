package cc.warlock.rcp.stormfront.adapters;

import java.util.ArrayList;

import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.rcp.ui.client.WarlockClientAdapterFactory;

public class StormFrontClientAdapterFactory extends WarlockClientAdapterFactory {

	@Override
	public Class[] getAdapterList() {
		
		ArrayList<Class> classes = new ArrayList<Class>();
		for (Class c : super.getAdapterList())
			classes.add(c);
		
		classes.add(IStormFrontClient.class);
		
		return classes.toArray(new Class[classes.size()]);
	}
}
