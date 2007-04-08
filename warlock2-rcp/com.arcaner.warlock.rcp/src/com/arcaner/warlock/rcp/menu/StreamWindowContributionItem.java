package com.arcaner.warlock.rcp.menu;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import com.arcaner.warlock.rcp.actions.OpenStreamWindowAction;
import com.arcaner.warlock.stormfront.internal.Stream;

public class StreamWindowContributionItem extends CompoundContributionItem {

	@Override
	protected IContributionItem[] getContributionItems() {
		String[] streamNames = new String[] { "thoughts" };
		IContributionItem[] items = new IContributionItem[streamNames.length];
		int i = 0;
		
		for (String streamName : streamNames)
		{
			OpenStreamWindowAction action = new OpenStreamWindowAction(Stream.fromName(streamName));
			items[i] = new ActionContributionItem(action);
			i++;
		}
		
		return items;
	}

}
