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

import java.util.ArrayList;

import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Label;

import cc.warlock.core.client.ICharacterStatus;
import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.rcp.ui.WarlockText;
import cc.warlock.rcp.ui.client.SWTPropertyListener;

/**
 * @author Will Robertson
 *
 */
public class StormFrontTextBorder implements PaintListener, IPropertyListener<String> {
	protected WarlockText textWidget;
	protected Boolean highlighted;
	protected Boolean stunned;
	protected Boolean bleeding;
	protected Color color;
	protected IStormFrontClient activeClient;
	protected ArrayList<IStormFrontClient> clients = new ArrayList<IStormFrontClient>();
	protected SWTPropertyListener<String> wrapper = new SWTPropertyListener<String>(this);
	
	public StormFrontTextBorder(WarlockText text) {
		textWidget = text; // Set Text Widget we'll be drawing on
		highlighted = false;
		text.addPaintListener(this);
		text.addControlListener(new ControlListener () {
			public void controlMoved(ControlEvent e) {}
			public void controlResized(ControlEvent e) {
				redraw();
			}
		});
		text.getVerticalBar().addSelectionListener(new SelectionListener () {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				redraw();
			}
		});
		text.addExtendedModifyListener(new ExtendedModifyListener() {
			public void modifyText(ExtendedModifyEvent e) {
				redraw();
			}
		});
	}
	
	private void setStunned() {
		if (!(highlighted && stunned)) {
			color = new Color(textWidget.getDisplay(),255,255,0);
			highlighted = true;
			stunned = true;
			bleeding = false;
			redraw();
		}
	}
	
	private void setBleeding() {
		if (!(highlighted && bleeding)) {
			color = new Color(textWidget.getDisplay(),255,0,0);
			highlighted = true;
			stunned = false;
			bleeding = true;
			redraw();
		}
	}
	
	private void setClear() {
		if (highlighted) {
			highlighted = false;
			stunned = false;
			bleeding = false;
			color = null; // Clear it out so that Java can Garbage Collect
			redraw();
		}
	}
	
	private void redraw() {
		if (highlighted)
			if (!textWidget.isDisposed())
				textWidget.redraw();
	}

	public void propertyActivated(IProperty<String> property) {}

	public void propertyChanged(IProperty<String> property, String oldValue) {
		if (property == null || property.getName() == null || activeClient == null) return;
		
		if ("characterStatus".equals(property.getName()))
		{
			ICharacterStatus status = activeClient.getCharacterStatus();
			if (status.getStatus().get(ICharacterStatus.StatusType.Stunned)
					&& status.getStatus().get(ICharacterStatus.StatusType.Bleeding)) {
				// If both are active, report stunned. 
				// There is generally nothing they can do about the bleeding when stunned anyway
				setStunned();
			} else if (status.getStatus().get(ICharacterStatus.StatusType.Stunned)) {
				setStunned();
			} else if (status.getStatus().get(ICharacterStatus.StatusType.Bleeding)) {
				setBleeding();
			} else {
				setClear();
			}
		}
		// TODO Auto-generated method stub
		
	}

	public void propertyCleared(IProperty<String> property, String oldValue) {}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
	 */
	public void paintControl(PaintEvent e) {
		drawBorder(e.gc);
	}

	private void drawBorder(GC gc) {
		if (highlighted) {
			gc.setLineWidth(6);
			gc.setForeground(color);
			gc.drawRectangle(textWidget.getBounds());
		}
	}
	
	public void setActiveClient (IStormFrontClient client)
	{
		if (client == null) return;
		
		this.activeClient = client;
		
		if (!clients.contains(client))
		{
			setClear();
			client.getCharacterStatus().addListener(wrapper);
			
			clients.add(client);
		} else {
			propertyChanged(client.getCharacterStatus(), null);
		}
	}
}
