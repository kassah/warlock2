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
/*
 * Created on Jan 1, 2005
 */
package cc.warlock.rcp.ui;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Marshall
 */
public class ComboField {
    private String string;
    private Combo combo;
    
	public ComboField (Composite parent, int style) {
		combo = new Combo(parent, style);
		
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				ComboField.this.string = combo.getText();
			}            
		});
		combo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			public void widgetSelected(SelectionEvent e) {
				ComboField.this.string = combo.getText();
			}
		});
		
	}

	public String getText() {
		return string;
	}
	
	public Combo getCombo ()
	{
		return combo;
	}
}
