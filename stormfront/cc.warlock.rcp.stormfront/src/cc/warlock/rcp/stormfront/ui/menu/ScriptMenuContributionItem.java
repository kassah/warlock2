package cc.warlock.rcp.stormfront.ui.menu;

import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.script.IScript;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.rcp.menu.SubMenuContributionItem;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;
import java.util.ArrayList;

public class ScriptMenuContributionItem extends CompoundContributionItem {
	
	// A class to retrieve the running scripts from a client.
	protected class CharacterContributionItem extends SubMenuContributionItem {
	
		private cc.warlock.core.stormfront.client.IStormFrontClient m_Client = null;
		
		public CharacterContributionItem(IStormFrontClient client) {
			super(client.getCharacterName().get());
			m_Client = client;
		}
		
		@Override
		protected IContributionItem[] getContributionItems() {
			// Add Menu Items
			ArrayList<IContributionItem> items = new ArrayList<IContributionItem>();

			
			for(IScript script : m_Client.getRunningScripts()) 
			{
				IContributionItem newItem = new ScriptContributionItem(script);
				items.add(newItem);
			}
			
			return items.toArray(new IContributionItem[items.size()]); 
		}		
	}
	
	public class CharacterNameAction extends Action {
		
		public CharacterNameAction (String name) {
			super(name);
		}
	}
	
	@Override
	protected IContributionItem[] getContributionItems() {
		// Add Menu Items
		ArrayList<IContributionItem> items = new ArrayList<IContributionItem>();
		
		java.util.List<IWarlockClient> clients = WarlockClientRegistry.getActiveClients();
		for(IWarlockClient client : clients) {
			if (client instanceof IStormFrontClient) {
				CharacterContributionItem newCharItem = new CharacterContributionItem((IStormFrontClient)client);
				items.add(newCharItem);
			}
		}

		return items.toArray(new IContributionItem[items.size()]); 
	}

}
