package cc.warlock.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.client.ICharacterStatus;
import cc.warlock.client.IProperty;
import cc.warlock.client.IPropertyListener;
import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.ui.client.SWTPropertyListener;

public class StatusView extends ViewPart implements IPropertyListener<String>
{
	public static final String VIEW_ID = "cc.warlock.rcp.views.StatusView";
	protected static StatusView _instance;
	protected Label[] statusLabels = new Label[5];
	protected IStormFrontClient client;
	protected SWTPropertyListener<String> wrapper = new SWTPropertyListener<String>(this);
	
	public StatusView() {
		_instance = this;
	}
	
	public static StatusView getDefault() {
		return _instance;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(5, true);
		layout.horizontalSpacing = 0;
		main.setLayout(layout);
		
		for (int i = 0; i < 5; i++)
		{
			statusLabels[i] = new Label(parent, SWT.BORDER);
			statusLabels[i].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_STATUS_BLANK));
			statusLabels[i].setBackground(new Color(main.getDisplay(), 0, 0, 0));
		}
		
		client = Warlock2Plugin.getDefault().getCurrentClient();
		client.getCharacterStatus().addListener(wrapper);
	}

	public void propertyActivated(IProperty<String> property) {}
	
	public void propertyChanged(IProperty<String> property, String oldValue) {
		if (property == null || property.getName() == null) return;
		
		if ("characterStatus".equals(property.getName()))
		{
			ICharacterStatus status = client.getCharacterStatus();
			
			if (status.getStatus().get(ICharacterStatus.StatusType.Standing))
			{
				statusLabels[4].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_STATUS_STAND));
			}
			else if (status.getStatus().get(ICharacterStatus.StatusType.Sitting))
			{
				statusLabels[4].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_STATUS_SIT));
			}
			else if (status.getStatus().get(ICharacterStatus.StatusType.Kneeling))
			{
				statusLabels[4].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_STATUS_KNEEL));
			}
			else if (status.getStatus().get(ICharacterStatus.StatusType.Prone))
			{
				statusLabels[4].setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_STATUS_LIE));
			}
			
			// need more images to implement the rest...
		}
	}
	
	public void propertyCleared(IProperty<String> property, String oldValue) {}
	
	@Override
	public void setFocus() {
//		this.client = Warlock2Plugin.getDefault().getCurrentClient();
	}

}
