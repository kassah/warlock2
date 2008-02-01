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
package cc.warlock.rcp.ui.client;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.rcp.ui.WarlockSharedImages;

public class WarlockClientAdaptable implements IAdaptable, IWorkbenchAdapter {

	protected IWarlockClient client;
	
	public WarlockClientAdaptable (IWarlockClient client)
	{
		this.client = client;
	}
	
	public Object getAdapter(Class adapter) {
		if (IWarlockClient.class.isAssignableFrom(adapter))
		{
			return this.client;
		}
		
		return null;
	}

	public Object[] getChildren(Object o) {
		return new Object[0];
	}
	
	public ImageDescriptor getImageDescriptor(Object object) {
		return WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_CHARACTER);
	}
	
	public String getLabel(Object o) {
		return client.getDefaultStream().getTitle().get();
	}
	
	public Object getParent(Object o) {
		return null;
	}
}
