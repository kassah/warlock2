package cc.warlock.rcp.actions;

import org.eclipse.jface.action.Action;

import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.views.GameView;
import cc.warlock.rcp.views.StreamView;

public class OpenStreamWindowAction extends Action {

	private String title, streamName, viewPrefix;
	
	public OpenStreamWindowAction (String title, String streamName, String viewPrefix)
	{
		super(title, Action.AS_CHECK_BOX);
		this.setImageDescriptor(WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_WINDOW));
		
		this.title = title;
		this.streamName = streamName;
		this.viewPrefix = viewPrefix;
	}
	
	@Override
	public void run() {
		
		StreamView streamView = StreamView.getViewForStream(viewPrefix, streamName);
		streamView.setViewTitle(title);
		streamView.setAppendNewlines(false);
		
		GameView inFocus = GameView.getViewInFocus();
		if (inFocus != null) {
			streamView.setClient(inFocus.getWarlockClient());
		}
		
		setChecked(true);
	}
	
	@Override
	public String getText() {
 		return title;
	}
}
