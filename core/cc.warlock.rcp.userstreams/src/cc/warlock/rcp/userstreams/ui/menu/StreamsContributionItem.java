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
 *
 */
public class StreamsContributionItem extends CompoundContributionItem  {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.actions.CompoundContributionItem#getContributionItems()
	 */
	@Override
	protected IContributionItem[] getContributionItems() {
		// TODO Auto-generated method stub
		//return null;
		//Collection<Profile> profiles = SavedProfiles.getAllProfiles();
		IContributionItem[] items = new IContributionItem[1]; //profiles.size()];
		//int i = 0;
		
		items[0] = new ActionContributionItem(new StreamShowAction());
		//for (Profile profile : profiles)
		//{
		//	items[i] = new ActionContributionItem(new ProfileConnectAction(profile));
		//	i++;
		//}
		
		return items; 
	}

}
