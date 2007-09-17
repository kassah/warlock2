/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.stormfront.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.WarlockClientAdapter;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.rcp.ui.WarlockProgressBar;
import cc.warlock.rcp.ui.client.SWTPropertyListener;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BarsView extends ViewPart implements IPropertyListener<Integer> {

	public static final String VIEW_ID = "cc.warlock.rcp.stormfront.ui.views.BarsView";
	
	protected static BarsView instance;
	protected Color roundtimeFG, roundtimeBG, roundtimeBorder,
		healthFG, healthBG, healthBorder,
		manaFG, manaBG, manaBorder,
		fatigueFG, fatigueBG, fatigueBorder,
		spiritFG, spiritBG, spiritBorder;
	
	protected WarlockProgressBar health, fatigue, spirit, mana, roundtime;
	
	protected SWTPropertyListener<Integer> listenerWrapper;
	
	public BarsView() {
		instance = this;
		listenerWrapper = new SWTPropertyListener<Integer>(this);
		
		WarlockClientRegistry.addWarlockClientListener(new WarlockClientAdapter() {
			public void clientConnected(final IWarlockClient client) {
				if (client instanceof IStormFrontClient)
				{
					Display.getDefault().syncExec(new Runnable() {
						public void run () {
							init((IStormFrontClient)client);
						}
					});
				}
			}
		});
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
		layout.marginWidth = layout.marginHeight = layout.horizontalSpacing = 0;
		top.setLayout(layout);
		top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Composite barComposite = new Composite(top, SWT.NONE);
		layout = new GridLayout(4, false);
		layout.marginWidth = layout.marginHeight = layout.horizontalSpacing = 0;
		barComposite.setLayout(layout);
		barComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
//		roundtime.setSize(150, 15);
		
		initBarColors();
		
//		new Label(barComposite, SWT.NONE).setText("roundtime: ");
		roundtime = new WarlockProgressBar(barComposite, SWT.NONE);
		roundtime.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, true, 7, 1));
		
//		new Label(barComposite, SWT.NONE).setText("health: ");
		health = new WarlockProgressBar(barComposite, SWT.NONE);
		health.setMinimum(0); health.setMaximum(100); health.setSelection(100); health.setLabel("health 100%");
		health.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, true));
		health.setBackground(healthBG); health.setForeground(healthFG); health.setBorderColor(healthBorder);
		
//		new Label(barComposite, SWT.NONE).setText("mana: ");
		mana = new WarlockProgressBar(barComposite, SWT.NONE);
		mana.setMinimum(0); mana.setMaximum(100); mana.setSelection(100); mana.setLabel("mana 100%");
		mana.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, true));
		mana.setBackground(manaBG); mana.setForeground(manaFG); mana.setBorderColor(manaBorder);
		
//		new Label(barComposite, SWT.NONE).setText("fatigue: ");
		fatigue = new WarlockProgressBar(barComposite, SWT.NONE);
		fatigue.setMinimum(0); fatigue.setMaximum(100); fatigue.setSelection(100); fatigue.setLabel("fatigue 100%");
		fatigue.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, true));
		fatigue.setBackground(fatigueBG); fatigue.setForeground(fatigueFG); fatigue.setBorderColor(fatigueBorder);
		
//		new Label(barComposite, SWT.NONE).setText("spirit: ");
		spirit = new WarlockProgressBar(barComposite, SWT.NONE);
		spirit.setMinimum(0); spirit.setMaximum(100); spirit.setSelection(100); spirit.setLabel("spirit 100%");
		spirit.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, true));
		spirit.setBackground(spiritBG); spirit.setForeground(fatigueFG); spirit.setBorderColor(spiritBorder);
		
		roundtime.setMinimum(0); roundtime.setMaximum(0); roundtime.setLabel("roundtime: 0");
		roundtime.setBackground(roundtimeBG); roundtime.setForeground(roundtimeFG); roundtime.setBorderColor(roundtimeBorder);
//		roundtime.setSize(300, 5); //roundtime.setShowText(false);
	}

	private void initBarColors() {
		Display display = getSite().getShell().getDisplay();
		
		healthBG = new Color(display, 0x80, 0, 0);
		healthFG = new Color(display, 255, 255, 255);
		healthBorder = new Color(display, 0x79, 0x6a, 0x6a);
		
		manaBG = new Color(display, 0, 0, 0xff);
		manaFG = new Color(display, 255, 255, 255);
		manaBorder = new Color(display, 0x72, 0x72, 0xff);
		
		fatigueBG = new Color(display, 0xd0, 0x98, 0x2f);
		fatigueFG = new Color(display, 0, 0, 0);
		fatigueBorder = new Color(display, 0xde, 0xcc, 0xaa);
		
		spiritBG = new Color(display, 150, 150, 150);
		spiritFG = new Color(display, 0, 0, 0);
		spiritBorder = new Color(display, 225, 225, 225);
		
		roundtimeBG = new Color(display, 151, 0, 0);
		roundtimeFG = new Color(display, 0, 0, 0);
		roundtimeBorder = new Color(display, 151, 130, 130);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private boolean activateRoundtime = false;
	public void propertyActivated(IProperty<Integer> property) {
		if (property == null || property.getName() == null) return;
		
		if ("roundtime".equals(property.getName()))
		{
			activateRoundtime = true;
		}
	}
	
	public void propertyCleared(IProperty<Integer> property, Integer oldValue) {	}
	public void propertyChanged(IProperty<Integer> property, Integer oldValue) {
		if (property == null || property.getName() == null) return;
		
		if (property.getName().equals("health"))
		{
			health.setSelection(property.get());
			health.setLabel("health: " + property.get() + "%");
		}
		else if (property.getName().equals("mana"))
		{
			mana.setSelection(property.get());
			mana.setLabel("mana: " + property.get() + "%");
		}
		else if (property.getName().equals("spirit"))
		{
			spirit.setSelection(property.get());
			spirit.setLabel("spirit: " + property.get() + "%");
		}
		else if (property.getName().equals("fatigue"))
		{
			fatigue.setSelection(property.get());
			fatigue.setLabel("fatigue: " + property.get() + "%");
		}
		else if (property.getName().equals("roundtime"))
		{
			if (activateRoundtime)
			{
				roundtime.setMaximum(property.get() * 1000);
				roundtime.setMinimum(0);
				activateRoundtime = false;
			}
			roundtime.setSelection(property.get() * 1000);
			roundtime.setLabel("roundtime: " + property.get() + " seconds");
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
