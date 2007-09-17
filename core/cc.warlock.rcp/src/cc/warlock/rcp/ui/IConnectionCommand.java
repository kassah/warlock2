package cc.warlock.rcp.ui;

import org.eclipse.swt.graphics.Image;

public interface IConnectionCommand {
	public String getLabel();
	public String getDescription();
	public Image getImage();
	
	public void run();
}
