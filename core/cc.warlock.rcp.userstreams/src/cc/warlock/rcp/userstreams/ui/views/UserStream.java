/**
 * 
 */
package cc.warlock.rcp.userstreams.ui.views;

import java.util.ArrayList;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.rcp.ui.StyleRangeWithData;
import cc.warlock.rcp.ui.WarlockText;
import cc.warlock.rcp.ui.client.SWTWarlockClientListener;
import cc.warlock.rcp.userstreams.IStreamFilter;
import cc.warlock.rcp.views.StreamView;

/**
 * @author Will Robertson
 * UserStreams
 * ViewPart/Stream View Class that shows user configurable content filtered from the main window.
 */
public class UserStream extends StreamView implements IWarlockClientListener {
	public static final String VIEW_ID = "cc.warlock.rcp.userstreams.rightView.userStream";
	protected static ArrayList<UserStream> openStreams = new ArrayList<UserStream>();
	// private ArrayList<IStreamFilter> filters = new ArrayList<IStreamFilter>();
	private String buffer = "";
	private IStreamFilter[] filters = null;
	private String name = "Stream";
	
	public void clientActivated(IWarlockClient client) {
		// TODO Auto-generated method stub
	}
	
	public void streamReceivedText (IStream stream, String string) {
		this.buffer += string;
	}
	
	public void setFilters(IStreamFilter[] filters) {
		this.filters = filters;
	}

	public void streamPrompted(IStream stream,	 String prompt) {
		// Discard Prompts
		String buffer = this.buffer;
		this.buffer = "";
		// TODO: We should probably make this go per line instead of per chunk sent to us.
		for (IStreamFilter filter : this.filters) {
			if (filter == null) continue;
			if (filter.match(buffer)) {
				// If a filter matches, we go ahead and display the chunk
				super.streamReceivedText(stream, buffer);
				return;
			}
		}
	}
	
	public void streamEchoed(IStream stream, String text) {
		// Discard Echos
	}
	
	protected void addStyleRange (WarlockText text, StyleRangeWithData range) {
		// Discard Styles for right now
	}
	
	public void streamReceivedStyle(IStream stream, IWarlockStyle style) {
		// Discard Styles for right now
	}
	
	public void clientConnected(IWarlockClient client) {
		setClient(client);
	}

	public void clientDisconnected(IWarlockClient client) {
		// TODO Auto-generated method stub
	}
	
	public void clientRemoved(IWarlockClient client) {
		// TODO Auto-generated method stub
	}

	public static UserStream getViewForUserStream (String streamName) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		for (UserStream view : openStreams)
		{
			if (view.getName().equals(streamName))
			{
				page.activate(view);
				return view;
			}
		}
		
		// none of the already created views match, create a new one
		try {
			UserStream nextInstance = (UserStream) page.showView(VIEW_ID , "rightFolder."+ streamName, IWorkbenchPage.VIEW_ACTIVATE);
			nextInstance.setName(streamName);
			nextInstance.setStreamName(IWarlockClient.DEFAULT_STREAM_NAME);
			nextInstance.setMultiClient(true);
			nextInstance.scanClients();
			
			return nextInstance;
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setName(String name) {
		this.name = name;
		this.setViewTitle(name);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void scanClients() {
		for (IWarlockClient client : WarlockClientRegistry.getActiveClients()) {
			clientConnected(client);
		}
	}

	public UserStream() {
		super();
		WarlockClientRegistry.addWarlockClientListener(new SWTWarlockClientListener(this));
	}
}
