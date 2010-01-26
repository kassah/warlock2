package cc.warlock.rcp.views;

import org.eclipse.ui.part.ViewPart;

public abstract class WarlockView extends ViewPart {
	static WarlockView viewInFocus;
	
	public static WarlockView getViewInFocus ()
	{
		return viewInFocus;
	}
	
	public void setFocus() {
		viewInFocus = this;
	}
	
	public abstract void pageUp();
	public abstract void pageDown();
}
