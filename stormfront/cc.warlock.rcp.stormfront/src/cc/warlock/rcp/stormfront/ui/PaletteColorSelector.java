package cc.warlock.rcp.stormfront.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

import cc.warlock.core.stormfront.client.StormFrontColor;
import cc.warlock.core.stormfront.serversettings.server.Palette;

public class PaletteColorSelector extends ColorSelector {

	protected Palette palette;
	protected StormFrontColor color;
	
	
	public PaletteColorSelector (Composite parent, Palette palette)
	{
		super(parent);
		
		this.palette = palette;
	}
	
	public void open() {
		PaletteColorDialog dialog = new PaletteColorDialog(getButton().getShell(), palette);
		if (color != null)
		{
			dialog.setColor(color);
		}
		int response = dialog.open();
		if (response == Dialog.OK)
		{
			RGB newColor = dialog.getRGB();
			if (newColor != null) {
			    RGB oldValue = getColorValue();
			    setColorValue(newColor);
			    color = dialog.getSelectedColor();
			    final Object[] finalListeners = getListeners();
			    if (finalListeners.length > 0) {
			        PropertyChangeEvent pEvent = new PropertyChangeEvent(
			                this, PROP_COLORCHANGE, oldValue, newColor);
			        for (int i = 0; i < finalListeners.length; ++i) {
			            IPropertyChangeListener listener = (IPropertyChangeListener) finalListeners[i];
			            listener.propertyChange(pEvent);
			        }
			    }
			    updateColorImage();
			}
		}
	}
	
	public StormFrontColor getStormFrontColor ()
	{
		return color;
	}
}
