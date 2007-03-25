/*
 * Created on Jan 1, 2005
 */
package com.arcaner.warlock.rcp.ui;

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
