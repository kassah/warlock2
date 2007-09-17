package cc.warlock.rcp.stormfront.ui.menu;

import java.util.Hashtable;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.rcp.actions.OpenStreamWindowAction;


public class StreamWindowContributionItem extends CompoundContributionItem {

	@Override
	protected IContributionItem[] getContributionItems() {	
		Hashtable <String, String> streams = new Hashtable<String,String>();
		streams.put(IStormFrontClient.THOUGHTS_STREAM_NAME, "Thoughts");
		streams.put(IStormFrontClient.INVENTORY_STREAM_NAME, "Inventory");
		streams.put(IStormFrontClient.DEATH_STREAM_NAME, "Deaths");
		
		IContributionItem[] items = new IContributionItem[streams.keySet().size()];
		int i = 0;
		
		for (String streamName : streams.keySet())
		{
			OpenStreamWindowAction action = new OpenStreamWindowAction(streams.get(streamName), streamName);
			items[i] = new ActionContributionItem(action);
			i++;
		}

		return items;
	}

}
