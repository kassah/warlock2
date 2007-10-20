/**
 * 
 */
package cc.warlock.rcp.userstreams.ui.actions;

import java.util.Random;

import org.eclipse.jface.action.Action;

import cc.warlock.rcp.userstreams.IStreamFilter;
import cc.warlock.rcp.userstreams.ui.views.UserStream;

/**
 * @author Will Robertson
 * Action for Selecting UserStreams from the Menu
 */
public class StreamShowAction extends Action {
	private String name;
	private IStreamFilter[] filters = null; 
	
	public StreamShowAction (String name, IStreamFilter[] filters) {
		super(name);
		this.name = name;
		this.filters = filters;
		setDescription("Custom output window: " + this.name);
	}
	
	public void run() {
		UserStream stream = UserStream.getViewForUserStream(this.name);
		stream.setFilters(filters);
	}
	
	protected static String generateUniqueId () {
		return new Random().nextInt() + "";
	}
}
