/**
 * 
 */
package cc.warlock.rcp.userstreams.ui.actions;

import java.util.Random;

import org.eclipse.jface.action.Action;

import cc.warlock.rcp.userstreams.ui.views.UserStream;

/**
 * @author Will Robertson
 * Action for Selecting UserStreams from the Menu
 */
public class StreamShowAction extends Action {
	String name;
	protected UserStream streamView = null;
	
	public StreamShowAction (String name) {
		super(name + " Stream");
		this.name = name;
		setDescription("Stream window: " + this.name);
	}
	
	public void run() {
		UserStream.getViewForUserStream(this.name);
	}
	
	protected static String generateUniqueId () {
		return new Random().nextInt() + "";
	}
}
