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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import cc.warlock.core.client.ICharacterStatus;
import cc.warlock.core.client.IProperty;
import cc.warlock.core.client.IPropertyListener;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.settings.IStormFrontClientSettings;
import cc.warlock.rcp.ui.client.SWTPropertyListener;
import cc.warlock.rcp.util.ColorUtil;

public class StormFrontStatus implements IPropertyListener<String> {

	protected Label[] statusLabels = new Label[4];
	protected IStormFrontClient activeClient;
	protected ArrayList<IStormFrontClient> clients = new ArrayList<IStormFrontClient>();
	protected SWTPropertyListener<String> wrapper = new SWTPropertyListener<String>(this);
	protected DecorationOverlayIcon multipleStatus;
	protected Image multipleStatusImage;
	
	public StormFrontStatus (Composite parent)
	{
		Composite main = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout(4, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		main.setLayout(layout);
//		main.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false, 1, 1));

		for (int i = 0; i < 4; i++)
		{
			statusLabels[i] = new Label(main, SWT.NONE);
			statusLabels[i].setImage(StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_STATUS_BLANK));
			GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
			data.minimumWidth = 24;
			statusLabels[i].setLayoutData(data);
		}
		
		setColors(new Color(main.getDisplay(), 240, 240, 255), new Color(main.getDisplay(), 25, 25, 50));
	}
	
	protected void clear ()
	{
		for (int i = 0; i < 4; i++)
		{
			statusLabels[i].setImage(StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_STATUS_BLANK));
		}
	}

	public void propertyActivated(IProperty<String> property) {}
	
	protected void setStatusImage(int place, String imageId)
	{
		if (statusLabels[place] == null) return;
		
		statusLabels[place].setImage(StormFrontSharedImages.getImage(imageId));
	}
	
	protected void handleMultipleStatus (ICharacterStatus status)
	{
		if (statusLabels[3] == null) return;
		
		Image baseImage = StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_STATUS_BLANK);
		ImageDescriptor overlays[] = new ImageDescriptor[] { null, null, null, null, null };
		
		if (status.getStatus().get(ICharacterStatus.StatusType.Bleeding))
		{
			overlays[0] = StormFrontSharedImages.getImageDescriptor(StormFrontSharedImages.IMG_STATUS_BLEEDING);
		}
		if (status.getStatus().get(ICharacterStatus.StatusType.Stunned))
		{
			overlays[1] = StormFrontSharedImages.getImageDescriptor(StormFrontSharedImages.IMG_STATUS_STUNNED);
		}
		if (status.getStatus().get(ICharacterStatus.StatusType.Webbed))
		{
			overlays[3] = StormFrontSharedImages.getImageDescriptor(StormFrontSharedImages.IMG_STATUS_WEBBED);
		}

		Image oldImage = multipleStatusImage;
		
		multipleStatus = new DecorationOverlayIcon(baseImage, overlays);
		multipleStatusImage = multipleStatus.createImage();
		
		statusLabels[3].setImage(multipleStatusImage);
		
		if (oldImage != null)
			oldImage.dispose();
	}
	
	public void propertyChanged(IProperty<String> property, String oldValue) {
		if (property == null || property.getName() == null || activeClient == null) return;
		
		if ("characterStatus".equals(property.getName()))
		{
			ICharacterStatus status = activeClient.getCharacterStatus();
			
			if (status.getStatus().get(ICharacterStatus.StatusType.Invisible)) 
			{
				if (status.getStatus().get(ICharacterStatus.StatusType.Standing))
				{
					setStatusImage(0, StormFrontSharedImages.IMG_STATUS_INVIS_STANDING);
				}
				else if (status.getStatus().get(ICharacterStatus.StatusType.Sitting))
				{
					setStatusImage(0, StormFrontSharedImages.IMG_STATUS_INVIS_SITTING);
				}
				else if (status.getStatus().get(ICharacterStatus.StatusType.Kneeling))
				{
					setStatusImage(0, StormFrontSharedImages.IMG_STATUS_INVIS_KNEELING);
				}
				else if (status.getStatus().get(ICharacterStatus.StatusType.Prone))
				{
					setStatusImage(0, StormFrontSharedImages.IMG_STATUS_INVIS_PRONE);
				}
			} else {
				if (status.getStatus().get(ICharacterStatus.StatusType.Standing))
				{
					setStatusImage(0, StormFrontSharedImages.IMG_STATUS_STANDING);
				}
				else if (status.getStatus().get(ICharacterStatus.StatusType.Sitting))
				{
					setStatusImage(0, StormFrontSharedImages.IMG_STATUS_SITTING);
				}
				else if (status.getStatus().get(ICharacterStatus.StatusType.Kneeling))
				{
					setStatusImage(0, StormFrontSharedImages.IMG_STATUS_KNEELING);
				}
				else if (status.getStatus().get(ICharacterStatus.StatusType.Prone))
				{
					setStatusImage(0, StormFrontSharedImages.IMG_STATUS_PRONE);
				}
			}
			
			if (status.getStatus().get(ICharacterStatus.StatusType.Joined))
			{
				setStatusImage(1, StormFrontSharedImages.IMG_STATUS_JOINED);
			}
			else {
				setStatusImage(1, StormFrontSharedImages.IMG_STATUS_BLANK);
			}
			
			if (status.getStatus().get(ICharacterStatus.StatusType.Hidden))
			{
				setStatusImage(2, StormFrontSharedImages.IMG_STATUS_HIDDEN);
			}
			else if (status.getStatus().get(ICharacterStatus.StatusType.Dead))
			{
				setStatusImage(2, StormFrontSharedImages.IMG_STATUS_DEAD);
			}
			else {
				setStatusImage(2, StormFrontSharedImages.IMG_STATUS_BLANK);
			}
			
			handleMultipleStatus(status);
		}
	}
	
	public void propertyCleared(IProperty<String> property, String oldValue) {}
		
	protected void setColors (Color fg, Color bg)
	{
		for (int i = 0; i < statusLabels.length; i++)
		{
			statusLabels[i].setForeground(fg);
			statusLabels[i].setBackground(bg);
		}
	}
	
	public void loadSettings (IStormFrontClientSettings settings)
	{
		if (settings.getMainWindowSettings() == null) return;
		
		Color bg = ColorUtil.warlockColorToColor(settings.getStormFrontClient().getStormFrontSkin().getMainBackground());
		Color fg = ColorUtil.warlockColorToColor(settings.getStormFrontClient().getStormFrontSkin().getMainForeground());
		
		setColors(fg, bg);
	}
	
	public void setActiveClient (IStormFrontClient client)
	{
		if (client == null) return;
		
		this.activeClient = client;
		
		if (!clients.contains(client))
		{
			clear();
			client.getCharacterStatus().addListener(wrapper);
			
			clients.add(client);
		} else {
			propertyChanged(client.getCharacterStatus(), null);
		}
		
		loadSettings(client.getStormFrontClientSettings());
	}
}
