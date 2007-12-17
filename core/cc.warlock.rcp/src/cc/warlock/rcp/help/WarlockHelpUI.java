package cc.warlock.rcp.help;

import org.eclipse.help.IContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.AbstractHelpUI;

import cc.warlock.rcp.ui.WarlockSharedImages;

public class WarlockHelpUI extends AbstractHelpUI {
	@Override
	public void displayHelp() {
		Shell shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		GridLayout layout = new GridLayout(1, false);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;	
		shell.setLayout(layout);
		shell.setText("Warlock Online Help");
		
		createContents(shell);
		
		shell.setSize(640, 480);
		shell.open();
	}
	
	protected void createContents (Shell parent)
	{
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(1, false));
		topComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Composite locationComposite = new Composite(topComposite, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = layout.marginWidth = 0;
		locationComposite.setLayout(layout);
		locationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		new Label(locationComposite, SWT.NONE).setImage(
			WarlockSharedImages.getImage(WarlockSharedImages.IMG_HELP_INDEX));
		new Label(locationComposite, SWT.NONE).setText(
			"Warlock Online Help > Help Index");
		
		Composite searchComposite = new Composite(topComposite, SWT.NONE);
		layout = new GridLayout(2, false);
		layout.marginHeight = layout.marginWidth = 0;
		
		searchComposite.setLayout(layout);
		searchComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Text searchText = new Text(searchComposite, SWT.BORDER);
		searchText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Button search = new Button(searchComposite, SWT.PUSH);
		search.setText("Search");
		search.setImage(WarlockSharedImages.getImage(WarlockSharedImages.IMG_HELP_SEARCH));
		
		Browser browser = new Browser(parent, SWT.BORDER);
		browser.setUrl("http://warlock.cc/wiki/index.php/Main_Page?useskin=monobook");
		
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	@Override
	public void displayHelpResource(String href) {
		System.out.println("display help resource: " + href);
	}

	@Override
	public boolean isContextHelpDisplayed() {
		System.out.println("is context help displayed?");
		return false;
	}
	
	@Override
	public void displayContext(IContext context, int x, int y) {
		System.out.println("display context: " + context + ", x:" + x +", y:" + y);
	}

}
