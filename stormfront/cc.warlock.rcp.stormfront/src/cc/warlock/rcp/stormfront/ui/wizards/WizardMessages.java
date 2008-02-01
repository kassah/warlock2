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
package cc.warlock.rcp.stormfront.ui.wizards;

import org.eclipse.osgi.util.NLS;

public class WizardMessages extends NLS {
	private static final String BUNDLE_NAME = "cc.warlock.rcp.stormfront.ui.wizards.messages"; //$NON-NLS-1$
	public static String AccountWizardPage_title;
	public static String AccountWizardPage_description;
	public static String AccountWizardPage_label_accountName;
	public static String AccountWizardPage_label_password;
	public static String AccountWizardPage_saveAccount_title;
	public static String AccountWizardPage_saveAccount_description;
	public static String AccountWizardPage_progressMessage;
	public static String AccountWizardPage_loginError_accountRejected;
	public static String AccountWizardPage_loginError_accountInvalid;
	public static String AccountWizardPage_loginError_passwordInvalid;
	public static String AccountWizardPage_loginError_title;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, WizardMessages.class);
	}
}
