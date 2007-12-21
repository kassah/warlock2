package cc.warlock.rcp.stormfront.ui;

import java.util.ArrayList;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import cc.warlock.core.stormfront.client.StormFrontColor;
import cc.warlock.core.stormfront.serversettings.server.Palette;
import cc.warlock.rcp.util.ColorUtil;

public class PaletteColorDialog extends Dialog {
 
	protected Palette palette;
	protected RGB rgb;
	protected ArrayList<ColorButton> paletteSelectors = new ArrayList<ColorButton>();
	
	public PaletteColorDialog (Shell parentShell, Palette palette)
	{
		super(parentShell);
		
		this.palette = palette;
	}
	
	public RGB getRGB ()
	{
		return rgb;
	}
	
	public StormFrontColor getSelectedColor ()
	{
		return selectedColor;
	}
	
	public void setColor (StormFrontColor color)
	{
		this.selectedColor = color;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, Dialog.OK, "OK", true);
		createButton(parent, Dialog.CANCEL, "Cancel", false);
	}
	
	protected StormFrontColor selectedColor;
	protected ColorSelector paletteColorSelector;
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label header = new Label(main, SWT.WRAP);
		header.setText("Select a color from the palette below:");
		header.setFont(JFaceResources.getBannerFont());
		header.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite selectedComposite = new Composite(main, SWT.NONE);
		selectedComposite.setLayout(new GridLayout(2, false));
		selectedComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		new Label(selectedComposite, SWT.NONE).setText("Selected color (click to change): ");
		paletteColorSelector = new ColorSelector(selectedComposite);
		paletteColorSelector.addListener(new IPropertyChangeListener () {
			public void propertyChange(PropertyChangeEvent event) {
				StormFrontColor newColor = new StormFrontColor(
					ColorUtil.rgbToWarlockColor(paletteColorSelector.getColorValue()));
				
				newColor.setPaletteId(selectedColor.getPaletteId());
//				palette.setPaletteColor(selectedColor.getPaletteId(), newColor);
				
				selectedColor = newColor;
				rgb = paletteColorSelector.getColorValue();
				paletteSelectors.get(Integer.parseInt(selectedColor.getPaletteId())).setColorValue(rgb);
			}
		});
		
		Composite paletteButtons = new Composite(main, SWT.NONE);
		paletteButtons.setLayout(new GridLayout(20, false));
		paletteButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		for (final StormFrontColor color : palette.getAllColors())
		{
			final ColorButton selector = new ColorButton(paletteButtons);
			selector.setColorValue(ColorUtil.warlockColorToRGB(color));
			
			selector.getButton().addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					selectColor(color);
				}
			});
			paletteSelectors.add(selector);
		}
		
		return main;
	}
	
	protected void selectColor (StormFrontColor color)
	{
		selectedColor = color;
		rgb = ColorUtil.warlockColorToRGB(color);
		
		paletteColorSelector.setColorValue(rgb);
	}
	
	/*
	 * This code is originally copied from org.eclipse.jface.preference.ColorSelector,
	 * and is licensed under the EPL.
	 * Original code (c) IBM and others
	 * - Removed property listener interface
	 * - Modified to make a smaller button (so we don't take up so much real estate with 109 palette entries)
	 */
	protected class ColorButton extends EventManager
	{
		protected Button fButton;
		protected Image fImage;
		protected Point fExtent;
		protected Color fColor;
		protected RGB fColorValue;
		
		public static final String PROP_COLORCHANGE = "colorValue";
		
		public ColorButton (Composite parent) {
			fButton = new Button(parent, SWT.PUSH);
			fExtent = new Point(15, 15);
			fImage = new Image(parent.getDisplay(), fExtent.x, fExtent.y);
			GC gc = new GC(fImage);
			gc.setBackground(fButton.getBackground());
			gc.fillRectangle(0, 0, fExtent.x, fExtent.y);
			gc.dispose();
			fButton.setImage(fImage);
			fButton.addDisposeListener(new DisposeListener() {
			    public void widgetDisposed(DisposeEvent event) {
			        if (fImage != null) {
			            fImage.dispose();
			            fImage = null;
			        }
			        if (fColor != null) {
			            fColor.dispose();
			            fColor = null;
			        }
			    }
			});
			fButton.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			    public void getName(AccessibleEvent e) {
			    	e.result = JFaceResources.getString("ColorSelector.Name"); //$NON-NLS-1$
			    }
			});
		}
		
		protected void updateColorImage() {
			Display display = fButton.getDisplay();
			GC gc = new GC(fImage);
			gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.drawRectangle(0, 2, fExtent.x - 1, fExtent.y - 4);
			if (fColor != null) {
				fColor.dispose();
			}
			fColor = new Color(display, fColorValue);
			gc.setBackground(fColor);
			gc.fillRectangle(1, 3, fExtent.x - 2, fExtent.y - 5);
			gc.dispose();
			fButton.setImage(fImage);
	    }
		
		public Button getButton () {
			return fButton;
		}
		
		public void addListener(IPropertyChangeListener listener) {
		    addListenerObject(listener);
		}
		
		public RGB getColorValue() {
	        return fColorValue;
	    }
		
		public void setColorValue(RGB rgb) {
		    fColorValue = rgb;
		    updateColorImage();
		}
	}
}
