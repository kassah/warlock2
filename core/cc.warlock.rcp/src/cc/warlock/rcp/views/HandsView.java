package cc.warlock.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
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
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(6, false);
		layout.horizontalSpacing = 0;
		main.setLayout(layout);
		
		leftHandImage = new Label(main, SWT.NONE);
		leftHandImage.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_LEFT_HAND_SMALL));
		leftHandText = new WarlockText(main, SWT.NONE);
		leftHandText.setText("Empty");
		leftHandText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		leftHandText.setLineSpacing(5);
		leftHandText.setIndent(5);
		
		rightHandImage = new Label(main, SWT.NONE);
		rightHandImage.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_RIGHT_HAND_SMALL));
		rightHandText = new WarlockText(main, SWT.NONE);
		rightHandText.setText("Empty");
		rightHandText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		rightHandText.setLineSpacing(5);
		rightHandText.setIndent(5);
		
		spellImage = new Label(main, SWT.NONE);
		spellImage.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_SPELL_HAND_SMALL));
		spellText = new WarlockText(main, SWT.NONE);
		spellText.setText("None");
		spellText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		spellText.setLineSpacing(5);
		spellText.setIndent(5);
		
		setColors(new Color(main.getDisplay(), 240, 240, 255), new Color(main.getDisplay(), 25, 25, 50));
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
