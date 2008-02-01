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
package cc.warlock.rcp.stormfront.ui.script;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.serversettings.server.IServerScriptInfo;
import cc.warlock.core.stormfront.serversettings.server.IServerScriptProvider;
import cc.warlock.rcp.stormfront.ui.StormFrontSharedImages;

public class StormfrontScriptLabelProvider implements ILabelProvider {

	public Image getImage(Object element) {
		if (element instanceof IServerScriptInfo)
		{
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
		}
		if (element instanceof IServerScriptProvider)
		{
			return StormFrontSharedImages.getImage(StormFrontSharedImages.IMG_STORMFRONT_SCRIPTS);
		}
		return null;
	}

	public String getText(Object element) {
		if (element instanceof IServerScriptInfo)
		{
			return ((IServerScriptInfo)element).getScriptName();
		}
		else if (element instanceof IServerScriptProvider)
		{
			IStormFrontClient client = ((IServerScriptProvider)element).getClient();
			
			return client.getCharacterName().get() + "'s Scripts";
		}
		return element.toString();
	}

	public void addListener(ILabelProviderListener listener) {	}

	public void dispose() {	}

	public boolean isLabelProperty(Object element, String property) {
		return true;
	}

	public void removeListener(ILabelProviderListener listener) {	}
}
