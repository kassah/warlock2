package cc.warlock.rcp.stormfront.ui.menu;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.rcp.actions.OpenStreamWindowAction;
import cc.warlock.rcp.views.StreamView;


public class StreamWindowContributionItem extends CompoundContributionItem {

	@Override
	protected IContributionItem[] getContributionItems() {	
		IContributionItem[] items = new IContributionItem[5];
		items[0] = streamContribution("Thoughts", IStormFrontClient.THOUGHTS_STREAM_NAME, StreamView.TOP_STREAM_PREFIX);
		items[1] = streamContribution("Inventory", IStormFrontClient.INVENTORY_STREAM_NAME, StreamView.RIGHT_STREAM_PREFIX);
		items[2] = streamContribution("Deaths", IStormFrontClient.DEATH_STREAM_NAME, StreamView.TOP_STREAM_PREFIX);
		items[3] = streamContribution("Current Room", IStormFrontClient.ROOM_STREAM_NAME, StreamView.RIGHT_STREAM_PREFIX);
		items[4] = streamContribution("Familiar", IStormFrontClient.FAMILIAR_STREAM_NAME, StreamView.RIGHT_STREAM_PREFIX);
		return items;
	}
	
	protected IContributionItem streamContribution(String label, String streamName, String prefix)
	{
		return new ActionContributionItem(new OpenStreamWindowAction(label, streamName, prefix));
	}

}
