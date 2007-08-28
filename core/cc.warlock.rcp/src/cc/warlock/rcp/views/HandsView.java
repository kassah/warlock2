package cc.warlock.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.client.IProperty;
import cc.warlock.client.IPropertyListener;
import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.configuration.server.ServerSettings;
import cc.warlock.configuration.skin.IWarlockSkin;
import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.ui.WarlockText;
import cc.warlock.rcp.ui.client.SWTPropertyListener;
import cc.warlock.rcp.util.ColorUtil;

public class HandsView extends ViewPart implements IPropertyListener<String> 
{
	public static final String VIEW_ID = "cc.warlock.rcp.views.HandsView";
	protected static HandsView _instance;
	
	protected Label leftHandImage, rightHandImage, spellImage;
	protected WarlockText leftHandText, rightHandText, spellText;
	protected IStormFrontClient client;
	
	public HandsView ()
	{
		_instance = this;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		parent.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(6, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		main.setLayout(layout);
		main.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		leftHandImage = createImageLabel(main, WarlockSharedImages.getImage(WarlockSharedImages.IMG_LEFT_HAND_SMALL));
		leftHandText = createText(main, "Empty");
		
		rightHandImage = createImageLabel(main, WarlockSharedImages.getImage(WarlockSharedImages.IMG_RIGHT_HAND_SMALL));
		rightHandText = createText(main, "Empty");
		
		spellImage = createImageLabel(main, WarlockSharedImages.getImage(WarlockSharedImages.IMG_SPELL_HAND_SMALL));
		spellText = createText(main, "None");
		
		setColors(new Color(main.getDisplay(), 240, 240, 255), new Color(main.getDisplay(), 25, 25, 50));
	}

	private Label createImageLabel(Composite main, Image image)
	{
		Label label = new Label(main, SWT.NONE);
		label.setImage(image);
		GridData data = new GridData(GridData.FILL, GridData.FILL, false, false);
		data.minimumHeight = 32;
		label.setLayoutData(data);
		
		return label;
	}
	
	private WarlockText createText (Composite main, String label)
	{
		WarlockText text = new WarlockText(main, SWT.NONE);
		text.setText(label);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
		data.minimumHeight = 30;
		data.minimumWidth = 30;
		text.setLayoutData(data);
		text.setLineSpacing(5);
		text.setIndent(5);
		
		return text;
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setClient (IStormFrontClient client)
	{
		this.client = client;
		
		client.getLeftHand().addListener(new SWTPropertyListener<String>(this));
		client.getRightHand().addListener(new SWTPropertyListener<String>(this));
		client.getCurrentSpell().addListener(new SWTPropertyListener<String>(this));
	}

	protected void setColors (Color fg, Color bg)
	{
		leftHandImage.setBackground(bg);
		leftHandImage.setForeground(fg);
		leftHandText.setBackground(bg);
		leftHandText.setForeground(fg);
		
		rightHandImage.setBackground(bg);
		rightHandImage.setForeground(fg);
		rightHandText.setBackground(bg);
		rightHandText.setForeground(fg);
		
		spellImage.setBackground(bg);
		spellImage.setForeground(fg);
		spellText.setBackground(bg);
		spellText.setForeground(fg);
	}
	
	public void loadServerSettings (ServerSettings settings)
	{
		Color bg = ColorUtil.warlockColorToColor(settings.getColorSetting(IWarlockSkin.ColorType.MainWindow_Background));
		Color fg = ColorUtil.warlockColorToColor(settings.getColorSetting(IWarlockSkin.ColorType.MainWindow_Foreground));
		
		setColors(fg, bg);
	}
	
	public void propertyActivated(IProperty<String> property) {}

	public void propertyChanged(IProperty<String> property, String oldValue) {
		if (property != null)
		{
			if (property.getName().equals("leftHand")) {
				leftHandText.setText(property.get());
			}
			else if (property.getName().equals("rightHand")) {
				rightHandText.setText(property.get());
			}
			else if (property.getName().equals("currentSpell")) {
				spellText.setText(property.get());
			}
		}
	}

	public void propertyCleared(IProperty<String> property, String oldValue) {
		if (property != null)
		{
			if (property.getName().equals("leftHand"))
				leftHandText.setText("<Empty>");
			else if (property.getName().equals("rightHand"))
				rightHandText.setText("<Empty>");
			else if (property.getName().equals("currentSpell"))
				spellText.setText("<None>");
		}
	}
	
	public static HandsView getDefault ()
	{
		return _instance;
	}
}
