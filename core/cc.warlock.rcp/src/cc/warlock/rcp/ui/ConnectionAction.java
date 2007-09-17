package cc.warlock.rcp.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.graphics.Image;

public class ConnectionAction implements IConnectionCommand {

	protected Action action;
	protected Image image;
	
	public ConnectionAction (Action action)
	{
		this.action = action;
		if (action.getImageDescriptor() != null) {
			this.image = action.getImageDescriptor().createImage();
		}
	}
	
	public String getDescription() {
		return action.getDescription();
	}

	public Image getImage() {
		return image;
	}

	public String getLabel() {
		return action.getText();
	}

	public void run() {
		action.run();
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (image != null)
			image.dispose();
		
		super.finalize();
	}

}
