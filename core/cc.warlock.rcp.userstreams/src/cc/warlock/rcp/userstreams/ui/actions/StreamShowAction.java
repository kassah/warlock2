/**
 * 
 */
package cc.warlock.rcp.userstreams.ui.actions;

import java.util.Random;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import cc.warlock.rcp.userstreams.ui.views.UserStream;

/**
 * @author Will Robertson
 *
 */
public class StreamShowAction extends Action {
	String name;
	protected UserStream streamView = null;
	
	public StreamShowAction (String name) {
		super(name + " Stream");
		this.name = name;
		setDescription("Stream window: " + this.name);
		//this.stream = new UserStream();
		//super(profile.getCharacterName(), StormFrontSharedImages.getImageDescriptor(StormFrontSharedImages.IMG_CHARACTER));
		//setDescription(profile.getGameName() + " character \"" + profile.getCharacterName() + "\"");
		
		//this.profile = profile;
	}
	
	public void run() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			if (streamView == null)
			{
				IViewPart part = page.showView(UserStream.VIEW_ID,UserStream.VIEW_ID + "."+ this.name, IWorkbenchPage.VIEW_ACTIVATE);
				if (part instanceof UserStream) {
					streamView = (UserStream) part;
					streamView.setStreamName(this.name);
				}
			} else {
				page.hideView(streamView);
				streamView = null;
			}
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static String generateUniqueId () {
		return new Random().nextInt() + "";
	}
}
