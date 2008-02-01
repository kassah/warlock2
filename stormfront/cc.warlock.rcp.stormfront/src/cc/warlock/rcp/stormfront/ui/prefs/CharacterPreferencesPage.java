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
package cc.warlock.rcp.stormfront.ui.prefs;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import cc.warlock.core.stormfront.client.IStormFrontClient;


public class CharacterPreferencesPage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.prefs.characterPrefs";
	
	protected IStormFrontClient client;
	
	public CharacterPreferencesPage() {
		// TODO Auto-generated constructor stub
	}

	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);

		new Label(main, SWT.NONE);
		
		setControl(main);
		return main;
	}

	@Override
	public void setElement(IAdaptable element) {
		client = (IStormFrontClient) element.getAdapter(IStormFrontClient.class);
		if (client != null)
		{
			setTitle(client.getCharacterName().get() + ": Preferences");
		}
	}
}
