package cc.warlock.rcp.prefs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

public class LookAndFeelPreferencePage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public static final String PAGE_ID = "cc.warlock.rcp.prefs.lookAndFeel";
	
	private IWorkbench workbench;
	
	protected Control createContents(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);

		new Label(main, SWT.NONE);
		
		setControl(main);
		return main;
	}
	
	
}
