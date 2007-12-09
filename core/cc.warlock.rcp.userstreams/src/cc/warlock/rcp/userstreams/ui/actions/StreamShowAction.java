/**
 * 
 */
package cc.warlock.rcp.userstreams.ui.actions;

import java.util.Random;

import org.eclipse.jface.action.Action;

import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.userstreams.IStreamFilter;
import cc.warlock.rcp.userstreams.ui.views.UserStream;

/**
 * @author Will Robertson
 * Action for Selecting UserStreams from the Menu
 */
public class StreamShowAction extends Action {
	private String name;
	
	public StreamShowAction (String name) {
		super(name);
		this.name = name;
		setDescription("Custom output window: " + this.name);
		setImageDescriptor(WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_WINDOW));
	}
	
	public void run() {
		UserStream.getViewForUserStream(this.name);
	}
	
	protected static String generateUniqueId () {
		return new Random().nextInt() + "";
	}
}
