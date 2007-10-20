/**
 * 
 */
package cc.warlock.rcp.userstreams.ui.menu;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import cc.warlock.rcp.userstreams.IStreamFilter;
import cc.warlock.rcp.userstreams.internal.StreamFilter;
import cc.warlock.rcp.userstreams.ui.actions.StreamShowAction;

/**
 * @author Will Robertson
 * Streams Menu Contribution - Adds all menu items to preferences.
 */
public class StreamsContributionItem extends CompoundContributionItem  {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.actions.CompoundContributionItem#getContributionItems()
	 */
	@Override
	protected IContributionItem[] getContributionItems() {
		// Add Menu Items
		IContributionItem[] items = new IContributionItem[2];
		IAction item;
		IStreamFilter[] filters = new IStreamFilter[5];
		filters[0] = new StreamFilter("^You've gained a new rank in (.+)\\.", IStreamFilter.type.regex);
		filters[1] = new StreamFilter("^Announcement: (.+)$", IStreamFilter.type.regex);
		filters[2] = new StreamFilter("^System Announcement: (.+)$", IStreamFilter.type.regex);
		filters[3] = new StreamFilter("^(Xibar|Katamba|Yavash) slowly rises above the horizon\\.", IStreamFilter.type.regex);
		filters[4] = new StreamFilter("^(Xibar|Katamba|Yavash) sets, slowly dropping below the horizon\\.", IStreamFilter.type.regex);
		item = new StreamShowAction("Events", filters);
		items[0] = new ActionContributionItem(item);
		filters = new IStreamFilter[26];
		filters[0] = new StreamFilter("says", IStreamFilter.type.string);
		filters[1] = new StreamFilter("asks", IStreamFilter.type.string);
		filters[2] = new StreamFilter("exclaims", IStreamFilter.type.string);
		filters[3] = new StreamFilter("whispers", IStreamFilter.type.string);
		filters[4] = new StreamFilter("stretch", IStreamFilter.type.string);
		filters[5] = new StreamFilter("yawn", IStreamFilter.type.string);
		filters[6] = new StreamFilter("chuckle", IStreamFilter.type.string);
		filters[7] = new StreamFilter("chortle", IStreamFilter.type.string);
		filters[8] = new StreamFilter("grin", IStreamFilter.type.string);
		filters[9] = new StreamFilter("beam", IStreamFilter.type.string);
		filters[10] = new StreamFilter("hug", IStreamFilter.type.string);
		filters[11] = new StreamFilter("applaud", IStreamFilter.type.string);
		filters[12] = new StreamFilter("babble", IStreamFilter.type.string);
		filters[13] = new StreamFilter("accusatory", IStreamFilter.type.string);
		filters[14] = new StreamFilter("blink", IStreamFilter.type.string);
		filters[16] = new StreamFilter("flush", IStreamFilter.type.string);
		filters[17] = new StreamFilter("\"Boo\"", IStreamFilter.type.string);
		filters[18] = new StreamFilter("bow", IStreamFilter.type.string);
		filters[19] = new StreamFilter("cackle", IStreamFilter.type.string);
		filters[20] = new StreamFilter("cringe", IStreamFilter.type.string);
		filters[21] = new StreamFilter("cower", IStreamFilter.type.string);
		filters[22] = new StreamFilter("weep", IStreamFilter.type.string);
		filters[23] = new StreamFilter("mumble", IStreamFilter.type.string);
		filters[24] = new StreamFilter("dance", IStreamFilter.type.string);
		filters[25] = new StreamFilter("^\\(.+\\)$", IStreamFilter.type.regex);
		//filters[3] = new StreamFilter("", IStreamFilter.type.regex);
		//filters[4] = new StreamFilter("", IStreamFilter.type.regex);
		//filters[5] = new StreamFilter("", IStreamFilter.type.regex);
		item = new StreamShowAction("Conversations", filters);
		items[1] = new ActionContributionItem(item);
		
		return items; 
	}

}
