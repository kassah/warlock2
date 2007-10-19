/**
 * 
 */
package cc.warlock.rcp.userstreams.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import cc.warlock.rcp.userstreams.ui.views.UserStream;

/**
 * @author Will Robertson
 *
 */
public class StreamShowAction extends Action {
	String name;
	protected IViewPart streamView = null;
	
	public StreamShowAction () {
		super("Says");
		setDescription("Stream window showing says");
		//this.stream = new UserStream();
		//super(profile.getCharacterName(), StormFrontSharedImages.getImageDescriptor(StormFrontSharedImages.IMG_CHARACTER));
		//setDescription(profile.getGameName() + " character \"" + profile.getCharacterName() + "\"");
		
		//this.profile = profile;
	}
	
	public void run() {
		try {
			if (streamView == null)
			{
				streamView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(UserStream.VIEW_ID);
			} else {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(streamView);
				streamView = null;
			}
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
