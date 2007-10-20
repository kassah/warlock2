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
		filters[0] = new StreamFilter("^You've gained a new rank in .+\\.", IStreamFilter.type.regex);
		filters[1] = new StreamFilter("^Announcement: .+$", IStreamFilter.type.regex);
		filters[2] = new StreamFilter("^System Announcement: .+$", IStreamFilter.type.regex);
		filters[3] = new StreamFilter("^(Xibar|Katamba|Yavash) slowly rises above the horizon\\.", IStreamFilter.type.regex);
		filters[4] = new StreamFilter("^(Xibar|Katamba|Yavash) sets, slowly dropping below the horizon\\.", IStreamFilter.type.regex);
		item = new StreamShowAction("Events", filters);
		items[0] = new ActionContributionItem(item);
		filters = new IStreamFilter[11];
		int i = -1;
		// (say|ask|exclaim|whisper)
		filters[++i] = new StreamFilter("^\\w+ \\w+( (at|to) \\w+)?, \".+\"$", IStreamFilter.type.regex);
		filters[++i] = new StreamFilter("thoughts in your head", IStreamFilter.type.string);
		filters[++i] = new StreamFilter("^\\w+ (nod|lean|stretch|smile|yawn|chuckle|chortle|grin|beam|hug|applaud|babble|blink|bow|cackle|cringe|cower|weep|mumble|wave|ponder|peers quizzically|snort|snuggle|cuddle|smirk|laugh|jumps back from|nods? slightly|whistles? a merry tune.)", IStreamFilter.type.regex);
		filters[++i] = new StreamFilter("accusatory", IStreamFilter.type.string);
		filters[++i] = new StreamFilter("flush", IStreamFilter.type.string);
		filters[++i] = new StreamFilter("\"Boo\"", IStreamFilter.type.string);
		filters[++i] = new StreamFilter("weep", IStreamFilter.type.string);
		filters[++i] = new StreamFilter("mumble", IStreamFilter.type.string);
		filters[++i] = new StreamFilter("dance", IStreamFilter.type.string);
		filters[++i] = new StreamFilter("tickle", IStreamFilter.type.string);
		filters[++i] = new StreamFilter("^\\(.+\\)$", IStreamFilter.type.regex);
		item = new StreamShowAction("Conversations", filters);
		items[1] = new ActionContributionItem(item);
		
		return items; 
	}

}
