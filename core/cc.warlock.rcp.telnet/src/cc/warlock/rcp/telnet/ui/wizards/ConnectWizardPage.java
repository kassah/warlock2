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
package cc.warlock.rcp.telnet.ui.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import cc.warlock.rcp.ui.ComboField;
import cc.warlock.rcp.ui.TextField;
import cc.warlock.rcp.ui.WarlockSharedImages;
import cc.warlock.rcp.wizards.WizardPageWithNotification;

/**
 * @author kassah
 *
 */
public class ConnectWizardPage extends WizardPageWithNotification {
	private ComboField host;
	private TextField port;
	private boolean hostComplete;
	private boolean portComplete;
	
	public ConnectWizardPage () {
		super (WizardMessages.ConnectWizardPage_title, WizardMessages.ConnectWizardPage_description,
				WarlockSharedImages.getImageDescriptor(WarlockSharedImages.IMG_WIZBAN_WARLOCK));
		setPageComplete(false);
		hostComplete = false;
		portComplete = false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		// Create Layout
		Composite controls = new Composite(parent, SWT.NONE);
		controls.setLayout(new GridLayout(1, false));
		
		// Create Host Entry Control
		new Label(controls, SWT.NONE).setText(WizardMessages.ConnectWizardPage_label_host);
		host = new ComboField(controls, SWT.BORDER | SWT.DROP_DOWN);
		host.getCombo().addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (e.widget instanceof Combo) {
					Combo control = (Combo) e.widget;
					if (control.getText().length() > 0) {
						hostComplete = true;
					} else {
						hostComplete = false;
					}
					checkComplete();
				}
			}
		});
		host.getCombo().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Create Port Entry Control
		new Label(controls, SWT.NONE).setText(WizardMessages.ConnectWizardPage_label_port);
		port = new TextField(controls, SWT.BORDER);
		Text control = port.getTextControl();
		
		// Use Verify Listener to restrict port to numeric
		control.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						e.doit = false;
						return;
					}
				}
			}
		});
		
		// Check for Port completeness so that we can mark page complete
		control.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (e.widget instanceof Text) {
					Text control = (Text) e.widget;
					if (control.getText().length() > 0) {
						portComplete = true;
					} else {
						portComplete = false;
					}
					checkComplete();
				}
			}
		});
		
		port.getTextControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		setControl(controls);
	}
	
	public String host() {
		return host.getText();
	}
	
	public int port() {
		return Integer.parseInt(port.getText());
	}
	
	public void checkComplete() {
		if (hostComplete && portComplete) {
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
	}
}
