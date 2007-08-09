package cc.warlock.rcp.prefs;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import cc.warlock.client.stormfront.IStormFrontClient;


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
