package com.arcaner.warlock.rcp.actions;

import org.eclipse.jface.action.Action;

import com.arcaner.warlock.rcp.views.GameView;
import com.arcaner.warlock.rcp.views.StreamView;

public class OpenStreamWindowAction extends Action {

	private String title, streamName;
	
	public OpenStreamWindowAction (String title, String streamName)
	{
		super(title, Action.AS_CHECK_BOX);
		this.title = title;
		this.streamName = streamName;
	}
	
	@Override
	public void run() {
		
		StreamView streamView = StreamView.getViewForStream(streamName);
		streamView.setAppendNewlines(true);
		
		GameView inFocus = GameView.getViewInFocus();
		if (inFocus != null) {
			streamView.setClient(inFocus.getStormFrontClient());
		}
		
		setChecked(true);
	}
	
	@Override
	public String getText() {
 		return title;
	}
}
