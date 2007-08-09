package com.arcaner.warlock.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import com.arcaner.warlock.client.IProperty;
import com.arcaner.warlock.client.IPropertyListener;
import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.rcp.ui.WarlockSharedImages;
import com.arcaner.warlock.rcp.ui.client.SWTPropertyListener;

public class HandsView extends ViewPart implements IPropertyListener<String> 
{
	public static final String VIEW_ID = "com.arcaner.warlock.rcp.views.HandsView";
	protected static HandsView _instance;
	
	protected Label leftHandText, rightHandText, spellText;
	protected IStormFrontClient client;
	
	public HandsView ()
	{
		_instance = this;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(6, false));
		
		new Label(main, SWT.NONE).setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_LEFT_HAND_SMALL));
		leftHandText = new Label(main, SWT.NONE);
		leftHandText.setText("Empty");
		leftHandText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		new Label(main, SWT.NONE).setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_RIGHT_HAND_SMALL));
		rightHandText = new Label(main, SWT.NONE);
		rightHandText.setText("Empty");
		rightHandText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		new Label(main, SWT.NONE).setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_SPELL_HAND_SMALL));
		spellText = new Label(main, SWT.NONE);
		spellText.setText("None");
		spellText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
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

	public void propertyActivated(IProperty<String> property) {}

	public void propertyChanged(IProperty<String> property, String oldValue) {
		if (property != null)
		{
			if (property.getName().equals("leftHand"))
				leftHandText.setText(property.get());
			else if (property.getName().equals("rightHand"))
				rightHandText.setText(property.get());
			else if (property.getName().equals("currentSpell"))
				spellText.setText(property.get());
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
