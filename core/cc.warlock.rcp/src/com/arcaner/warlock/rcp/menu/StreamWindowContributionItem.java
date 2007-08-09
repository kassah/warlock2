package com.arcaner.warlock.rcp.menu;

import java.util.Hashtable;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.rcp.actions.OpenStreamWindowAction;

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
