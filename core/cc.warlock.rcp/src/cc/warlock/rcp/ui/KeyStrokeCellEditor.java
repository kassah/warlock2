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

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import cc.warlock.rcp.ui.KeyStrokeText.KeyStrokeLockListener;

/**
 * @author Marshall Culpepper
 *
 */
public class KeyStrokeCellEditor extends CellEditor {

	public KeyStrokeCellEditor (Composite parent, int style)
	{
		super(parent, style);
	}
	
	protected KeyStrokeText keyText;
	protected Button ok, cancel;
	
	@Override
	protected Control createControl(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = layout.marginWidth = 0;
		layout.verticalSpacing = layout.horizontalSpacing = 0;
		main.setLayout(layout);
		
		keyText = new KeyStrokeText(main, SWT.SINGLE);
		keyText.getText().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		keyText.getText().setFont(parent.getFont());
		keyText.getText().addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                KeyStrokeCellEditor.this.focusLost();
            }
        });
		keyText.addKeyStrokeLockListener(new KeyStrokeLockListener() {
			public void keyStrokeLocked() {
				ok.setEnabled(true);
			}
			public void keyStrokeUnlocked() {
				ok.setEnabled(false);
			}
		});
		
		ok = new Button(main, SWT.PUSH);
		ok.setText("OK");
		ok.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				okSelected();
			}
		});
		ok.setEnabled(false);
		
		cancel = new Button(main, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				cancelSelected();
			}
		});
		
		return main;
	}
	
	protected void cancelSelected() {
		fireCancelEditor();
	}

	protected void okSelected() {
		fireApplyEditorValue();
		deactivate();
	}
	
	@Override
	protected Object doGetValue() {
		return keyText.getKeyStroke();
	}

	@Override
	protected void doSetFocus() {
		keyText.getText().setFocus();
	}

	@Override
	protected void doSetValue(Object value) {
		keyText.setKeyStroke((KeyStroke)value);
		keyText.getText().setText(SWTKeySupport.getKeyFormatterForPlatform().format((KeyStroke)value));
	}

}
