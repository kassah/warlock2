package com.arcaner.warlock.rcp.actions;

import org.eclipse.jface.action.Action;

import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.rcp.views.GameView;
import com.arcaner.warlock.rcp.views.StreamView;
import com.arcaner.warlock.stormfront.IStream;

public class OpenStreamWindowAction extends Action {

	private IStream stream;
	
	public OpenStreamWindowAction (IStream stream)
	{
		super(stream.getTitle() == null ? stream.getName() : stream.getTitle(), Action.AS_CHECK_BOX);
		
		this.stream = stream;
	}
	
	@Override
	public void run() {
		
		StreamView streamView = StreamView.createNext(stream.getName());
		streamView.setStream(stream);
		
		GameView inFocus = GameView.getViewInFocus();
		if (inFocus != null) {
			streamView.setClient((IStormFrontClient) inFocus.getClient());
		}
	}
	
	@Override
	public String getText() {
 		return stream.getTitle() == null ? stream.getName() : stream.getTitle();
	}
}
