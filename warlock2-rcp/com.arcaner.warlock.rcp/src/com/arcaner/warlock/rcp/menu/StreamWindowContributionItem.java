package com.arcaner.warlock.rcp.menu;

import java.util.Hashtable;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import com.arcaner.warlock.rcp.actions.OpenStreamWindowAction;

public class StreamWindowContributionItem extends CompoundContributionItem {

	@Override
	protected IContributionItem[] getContributionItems() {	
		Hashtable <String, String> streams = new Hashtable<String,String>();
		streams.put("thoughts", "Thoughts");
		streams.put("inv", "Inventory");
		
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
