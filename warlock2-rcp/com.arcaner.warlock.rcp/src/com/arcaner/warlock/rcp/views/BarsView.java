/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.part.ViewPart;

import com.arcaner.warlock.client.IProperty;
import com.arcaner.warlock.client.IPropertyListener;
import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.rcp.ui.client.SWTPropertyListener;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BarsView extends ViewPart implements IPropertyListener<Integer> {

	public static final String VIEW_ID = "com.arcaner.warlock.rcp.views.barsView";
	
	protected static BarsView instance;
	protected ProgressBar roundtime, health, mana, fatigue, spirit;
	protected Color roundtimeFG, roundtimeBG, healthFG, healthBG, manaFG, manaBG, fatigueFG, fatigueBG, spiritFG, spiritBG;
	protected SWTPropertyListener<Integer> listenerWrapper;
	
	public BarsView() {
		instance = this;
		listenerWrapper = new SWTPropertyListener<Integer>(this);
	}

	public void init (IStormFrontClient client)
	{		
		client.getHealth().addListener(listenerWrapper);
		client.getMana().addListener(listenerWrapper);
		client.getSpirit().addListener(listenerWrapper);
		client.getFatigue().addListener(listenerWrapper);
		client.getRoundtime().addListener(listenerWrapper);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		Composite top = new Composite (parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		top.setLayout(layout);
		
		Composite barComposite = new Composite(top, SWT.NONE);
		barComposite.setLayout(new GridLayout(8, false));
		
//		roundtime.setSize(150, 15);
		
		initBarColors();
		
		new Label(barComposite, SWT.NONE).setText("roundtime: ");
		roundtime = new ProgressBar(barComposite, SWT.NONE);
		roundtime.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false, 7, 1));
		
		new Label(barComposite, SWT.NONE).setText("health: ");
		health = new ProgressBar(barComposite, SWT.NONE);
		health.setMinimum(0); health.setMaximum(100); health.setSelection(100); //health.setLabel("health 100%");
		health.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false));
		health.setBackground(healthBG); health.setForeground(healthFG);
		
		new Label(barComposite, SWT.NONE).setText("mana: ");
		mana = new ProgressBar(barComposite, SWT.NONE);
		mana.setMinimum(0); mana.setMaximum(100); mana.setSelection(100); //mana.setLabel("mana 100%");
		mana.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false));
		mana.setBackground(manaBG); mana.setForeground(manaFG);
		
		new Label(barComposite, SWT.NONE).setText("fatigue: ");
		fatigue = new ProgressBar(barComposite, SWT.NONE);
		fatigue.setMinimum(0); fatigue.setMaximum(100); fatigue.setSelection(100); //fatigue.setLabel("fatigue 100%");
		fatigue.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false));
		fatigue.setBackground(fatigueBG); fatigue.setForeground(fatigueFG);
		
		new Label(barComposite, SWT.NONE).setText("spirit: ");
		spirit = new ProgressBar(barComposite, SWT.NONE);
		spirit.setMinimum(0); spirit.setMaximum(100); spirit.setSelection(100); //spirit.setLabel("spirit 100%");
		spirit.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false));
		spirit.setBackground(spiritBG); spirit.setForeground(fatigueFG);
		
		roundtime.setMinimum(0); roundtime.setMaximum(0); //roundtime.setLabel("roundtime: 0");
		roundtime.setBackground(roundtimeBG); roundtime.setForeground(roundtimeFG);
//		roundtime.setSize(300, 5); //roundtime.setShowText(false);
	}

	private void initBarColors() {
		Display display = getSite().getShell().getDisplay();
		
		healthFG = new Color(display, 128, 0, 0);
		healthBG = new Color(display, 255, 255, 255);
		manaFG = new Color(display, 0, 0, 255);
		manaBG = new Color(display, 255, 255, 255);
		fatigueFG = new Color(display, 208, 152, 47);
		fatigueBG = new Color(display, 0, 0, 0);
		spiritFG = new Color(display, 192, 192, 192);
		spiritBG = new Color(display, 0, 0, 0);
		roundtimeFG = new Color(display, 0, 128, 0);
		roundtimeBG = new Color(display, 30, 0, 30);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	public void propertyActivated(IProperty<Integer> property) {	}
	public void propertyCleared(IProperty<Integer> property, Integer oldValue) {	}
	public void propertyChanged(IProperty<Integer> property, Integer oldValue) {
		if (property.getName().equals("health"))
		{
			health.setSelection(property.get());
		}
		else if (property.getName().equals("mana"))
		{
			mana.setSelection(property.get());
		}
		else if (property.getName().equals("spirit"))
		{
			spirit.setSelection(property.get());
		}
		else if (property.getName().equals("fatigue"))
		{
			fatigue.setSelection(property.get());
		}
		else if (property.getName().equals("roundtime"))
		{
			roundtime.setMaximum(property.get() * 1000);
			roundtime.setMinimum(0);
			roundtime.setSelection(property.get() * 1000);
			System.out.println("start roundtime: " + property.get() * 1000);
		}
	}
	
	
	public void roundtimeChanged(IWarlockClient source, final int roundtime) {
		BarsView.this.roundtime.setSelection(roundtime);
	}
	
	public static BarsView getDefault ()
	{
		return instance;
	}
}
