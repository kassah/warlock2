package com.arcaner.warlock.rcp.prefs;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class LookAndFeelPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	public static final String PAGE_ID = "com.arcaner.warlock.rcp.prefs.lookAndFeel";
	
	private IWorkbench workbench;
	
	public LookAndFeelPreferencePage() {
		super ("Highlights Preferences");
	}

	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);

		new Label(main, SWT.NONE).setText("yo");
		
		setControl(main);
		return main;
	}

	public void init(IWorkbench workbench) {
		this.workbench = workbench;
	}

	@Override
	public boolean performOk() {
		// TODO Auto-generated method stub
		return super.performOk();
	}
}
