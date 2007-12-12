/**
 * 
 */
package cc.warlock.rcp.userstreams.ui.menu;

import java.util.ArrayList;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import cc.warlock.rcp.userstreams.ui.actions.StreamShowAction;

/**
 * @author Will Robertson
 * Streams Menu Contribution - Adds all menu items to preferences.
 */
public class StreamsContributionItem extends CompoundContributionItem  {

	// Moved hard settings to cc.warlock.userstreams.ui.views/UserStream.java

	protected IContributionItem createStreamContributionItem (String name)
	{
		return new ActionContributionItem(new StreamShowAction(name));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.actions.CompoundContributionItem#getContributionItems()
	 */
	@Override
	protected IContributionItem[] getContributionItems() {
		// Add Menu Items
		ArrayList<IContributionItem> items = new ArrayList<IContributionItem>();
		items.add(createStreamContributionItem("Events"));
		items.add(createStreamContributionItem("Conversations"));
		
		return items.toArray(new IContributionItem[items.size()]); 
	}
}
