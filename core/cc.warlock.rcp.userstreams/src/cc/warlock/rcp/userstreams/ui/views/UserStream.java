/**
 * 
 */
package cc.warlock.rcp.userstreams.ui.views;

import cc.warlock.rcp.ui.client.SWTWarlockClientListener;
import cc.warlock.rcp.views.StreamView;
import cc.warlock.core.client.IStream;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;
import cc.warlock.core.client.WarlockClientRegistry;
//import cc.warlock.core.client.IStreamListener;

/**
 * @author Will Robertson
 *
 */
public class UserStream extends StreamView implements IWarlockClientListener {
	private SWTWarlockClientListener clientListenerWrapper;
	public static final String VIEW_ID = "cc.warlock.rcp.userstreams.rightView.userStream";
	
	public void clientActivated(IWarlockClient client) {
		// TODO Auto-generated method stub
		
	}
	
	public void streamReceivedText (IStream stream, String string) {
		super.streamReceivedText(stream, string);
	}

	public void streamPrompted(IStream stream, String prompt) {
		// super.streamPrompted(stream, prompt);
	}
	
	public void streamEchoed(IStream stream, String text) {
		// Discard
	}
	
	public void clientConnected(IWarlockClient client) {
		//mainStream = client.getDefaultStream();
		//client.getDefaultStream().addStreamListener(this);
		this.addStream(client.getDefaultStream());
	}
	
	public void clientDisconnected(IWarlockClient client) {
		//mainStream = null;
		 client.getDefaultStream().removeStreamListener(streamListenerWrapper);
	}

	
	public void clientRemoved(IWarlockClient client) {
		// TODO Auto-generated method stub
		
	}
	
	/* public void setStreamName(String streamName) {
		this.setStreamName(streamName);
		this.setPartName(streamName);
	} */

	public UserStream() {
		// Constructor
		super();
		//setStreamName(name);
		clientListenerWrapper = new SWTWarlockClientListener(this);
		WarlockClientRegistry.addWarlockClientListener(clientListenerWrapper); // new SWTWarlockClientListener(this));
	}
}
