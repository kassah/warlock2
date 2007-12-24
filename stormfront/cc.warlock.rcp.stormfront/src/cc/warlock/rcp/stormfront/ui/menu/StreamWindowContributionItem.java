package cc.warlock.rcp.stormfront.ui.menu;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.network.StormFrontConnection;
import cc.warlock.rcp.actions.OpenStreamWindowAction;
import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.ui.network.SWTConnectionListenerAdapter;
import cc.warlock.rcp.views.DebugView;
import cc.warlock.rcp.views.StreamView;


public class StreamWindowContributionItem extends CompoundContributionItem {

	private class DebugAction extends Action {
		
		private static final String title = "Debug";
		
		public DebugAction() {
			super(title, Action.AS_CHECK_BOX);
		}
		
		public void run() {
			IWarlockClient client = Warlock2Plugin.getDefault().getCurrentClient();
			StormFrontConnection connection = (StormFrontConnection)client.getConnection();
			
			try {
				DebugView view = (DebugView)
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DebugView.VIEW_ID, connection.getKey(), IWorkbenchPage.VIEW_VISIBLE);
				view.setClient(client);
				connection.addConnectionListener(new SWTConnectionListenerAdapter(view));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public String getText() {
	 		return title;
		}
	}
	
	@Override
	protected IContributionItem[] getContributionItems() {	
		IContributionItem[] items = new IContributionItem[6];
		items[0] = streamContribution("Thoughts", IStormFrontClient.THOUGHTS_STREAM_NAME, StreamView.TOP_STREAM_PREFIX);
		items[1] = streamContribution("Inventory", IStormFrontClient.INVENTORY_STREAM_NAME, StreamView.RIGHT_STREAM_PREFIX);
		items[2] = streamContribution("Deaths", IStormFrontClient.DEATH_STREAM_NAME, StreamView.TOP_STREAM_PREFIX);
		items[3] = streamContribution("Current Room", IStormFrontClient.ROOM_STREAM_NAME, StreamView.RIGHT_STREAM_PREFIX);
		items[4] = streamContribution("Familiar", IStormFrontClient.FAMILIAR_STREAM_NAME, StreamView.RIGHT_STREAM_PREFIX);
		items[5] = new ActionContributionItem(new DebugAction());
		return items;
	}
	
	protected IContributionItem streamContribution(String label, String streamName, String prefix)
	{
		return new ActionContributionItem(new OpenStreamWindowAction(label, streamName, prefix));
	}

}
