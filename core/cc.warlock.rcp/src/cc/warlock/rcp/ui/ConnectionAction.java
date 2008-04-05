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
package cc.warlock.rcp.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.graphics.Image;

public class ConnectionAction implements IConnectionCommand {

	protected Action action;
	protected Image image;
	
	public ConnectionAction (Action action)
	{
		this.action = action;
		if (action.getImageDescriptor() != null) {
			this.image = action.getImageDescriptor().createImage();
		}
	}
	
	public String getDescription() {
		return action.getDescription();
	}

	public Image getImage() {
		return image;
	}

	public String getLabel() {
		return action.getText();
	}

	public void run() {
		action.run();
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (image != null)
			image.dispose();
		
		super.finalize();
	}

}
