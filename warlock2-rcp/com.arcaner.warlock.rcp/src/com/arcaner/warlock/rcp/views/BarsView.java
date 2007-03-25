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

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.stormfront.IStormFrontClientListener;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BarsView extends ViewPart implements IStormFrontClientListener {

	public static final String VIEW_ID = "com.arcaner.warlock.views.barsView";
	
	protected static BarsView instance;
	protected ProgressBar roundtime, health, mana, fatigue, spirit;
	protected Color roundtimeFG, roundtimeBG, healthFG, healthBG, manaFG, manaBG, fatigueFG, fatigueBG, spiritFG, spiritBG;
	
	public BarsView() {
		instance = this;
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
//		health.setBackground(healthBG); health.setForeground(healthFG);
		
		new Label(barComposite, SWT.NONE).setText("mana: ");
		mana = new ProgressBar(barComposite, SWT.NONE);
		mana.setMinimum(0); mana.setMaximum(100); mana.setSelection(100); //mana.setLabel("mana 100%");
		mana.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false));
//		mana.setBackground(manaBG); mana.setForeground(manaFG);
		
		new Label(barComposite, SWT.NONE).setText("fatigue: ");
		fatigue = new ProgressBar(barComposite, SWT.NONE);
		fatigue.setMinimum(0); fatigue.setMaximum(100); fatigue.setSelection(100); //fatigue.setLabel("fatigue 100%");
		fatigue.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false));
//		fatigue.setBackground(fatigueBG); fatigue.setForeground(fatigueFG);
		
		new Label(barComposite, SWT.NONE).setText("spirit: ");
		spirit = new ProgressBar(barComposite, SWT.NONE);
		spirit.setMinimum(0); spirit.setMaximum(100); spirit.setSelection(100); //spirit.setLabel("spirit 100%");
		spirit.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false));
//		spirit.setBackground(spiritBG); spirit.setForeground(fatigueFG);
		
		roundtime.setMinimum(0); roundtime.setMaximum(0); //roundtime.setLabel("roundtime: 0");
//		roundtime.setBackground(roundtimeBG); roundtime.setForeground(roundtimeFG);
//		roundtime.setSize(300, 5); //roundtime.setShowText(false);
	}

	private void initBarColors() {
		Display display = getSite().getShell().getDisplay();
		
		healthBG = new Color(display, 128, 0, 0);
		healthFG = new Color(display, 255, 255, 255);
		manaBG = new Color(display, 0, 0, 255);
		manaFG = new Color(display, 255, 255, 255);
		fatigueBG = new Color(display, 208, 152, 47);
		fatigueFG = new Color(display, 0, 0, 0);
		spiritBG = new Color(display, 192, 192, 192);
		spiritFG = new Color(display, 0, 0, 0);
		roundtimeBG = new Color(display, 0, 128, 0);
		roundtimeFG = new Color(display, 30, 0, 30);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void healthChanged(IWarlockClient source, final int health, final String label) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run () {
				BarsView.this.health.setSelection(health);
			}
		});
	}
	
	public void manaChanged(IWarlockClient source, final int mana, final String label) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run () {
				BarsView.this.mana.setSelection(mana);
			}
		});
	}
	
	public void fatigueChanged(IWarlockClient source, final int fatigue, final String label) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run () {
				BarsView.this.fatigue.setSelection(fatigue);
			}
		});
	}
	
	public void spiritChanged(IWarlockClient source, final int spirit, final String label) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run () {
				BarsView.this.spirit.setSelection(spirit);
			}
		});
	}

	public void roundtimeStarted(IWarlockClient source, final int roundtime) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run () {
				BarsView.this.roundtime.setMaximum(roundtime * 1000);
				BarsView.this.roundtime.setMinimum(0);
				BarsView.this.roundtime.setSelection(roundtime * 1000);
				System.out.println("start roundtime: " + roundtime * 1000);
			}
		});
	}
	
	public void roundtimeChanged(IWarlockClient source, final int roundtime) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run () {
				BarsView.this.roundtime.setSelection(roundtime);
				System.out.println("set roundtime: " + roundtime);
			}
		});
	}
	
	public void addWarlockClient(IWarlockClient client) {
	}

	public void setActiveClient (IWarlockClient active) {
	}
	
	public static BarsView getDefault ()
	{
		return instance;
	}
}
