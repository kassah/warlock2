/**
 * 
 */
package cc.warlock.rcp.userstreams.ui.menu;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

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
		
		items[0] = new ActionContributionItem(new StreamShowAction("Says"));
		items[1] = new ActionContributionItem(new StreamShowAction("Emotes"));
		
		return items; 
	}

}
