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
/**
 * 
 */
package cc.warlock.rcp.views;

import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;

/**
 *
 * Creates a Composite View to put further ViewParts in.
 * TODO: Make Listeners to pass  appropriate calls onto the Composite
 *
 * @author kassah
 */
public class CompositeView extends ViewPart {

	protected Composite container;

	public CompositeView() {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
	}
	
	@Override
	public void setFocus() {
		container.setFocus();
	}
	
	
	/**
	 * Gets the Composite container
	 * @return container
	 */
	public Composite getComposite() {
		return container;
	}
}
