/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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
