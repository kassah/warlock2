package cc.warlock.rcp.stormfront.ui.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.actions.CompoundContributionItem;

import cc.warlock.core.configuration.Profile;
import cc.warlock.core.stormfront.ProfileConfiguration;
import cc.warlock.rcp.stormfront.ui.StormFrontSharedImages;
import cc.warlock.rcp.stormfront.ui.actions.ProfileConnectAction;
import cc.warlock.rcp.ui.ConnectionAction;
import cc.warlock.rcp.ui.IConnectionCommand;
import cc.warlock.rcp.ui.IConnectionCommandProvider;


public class ProfileConnectContributionItem extends CompoundContributionItem implements IConnectionCommandProvider {

	public ProfileConnectContributionItem() {
		super();
	}
	
	public ProfileConnectContributionItem(String id) {
		super(id);
	}

	@Override
	protected IContributionItem[] getContributionItems() {
		Collection<Profile> profiles = ProfileConfiguration.instance().getAllProfiles();
		IContributionItem[] items = new IContributionItem[profiles.size()];
		int i = 0;
		
		for (Profile profile : profiles)
		{
			items[i] = new ActionContributionItem(new ProfileConnectAction(profile));
			i++;
		}
		
		return items;
	}
	
	public List<IConnectionCommand> getConnectionCommands () {
		ArrayList<IConnectionCommand> commands = new ArrayList<IConnectionCommand>();
		
		for (Profile profile : ProfileConfiguration.instance().getAllProfiles())
		{
			commands.add(new ConnectionAction(new ProfileConnectAction(profile)));
		}
		
		return commands;
	}
	
	public String getDescription(IHandler handler) {
		ProfileConnectAction phandler = (ProfileConnectAction)handler;
		
		return phandler.getProfile().getGameName() + ": " + phandler.getProfile().getName(); 
	}
	
	public Image getImage(IHandler handler) {
		return StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_CHARACTER);
	}
	
	public String getLabel(IHandler handler) {
		return ((ProfileConnectAction)handler).getProfile().getName();
	}
}
