package cc.warlock.scribe.ui.views;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.client.stormfront.IStormFrontClient;
import cc.warlock.rcp.ui.client.SWTScriptListener;
import cc.warlock.rcp.views.GameView;
import cc.warlock.scribe.ui.ScribeSharedImages;
import cc.warlock.script.IScript;
import cc.warlock.script.IScriptListener;

public class ScriptControlView extends ViewPart implements IScriptListener {

	protected IStormFrontClient client;
	protected SWTScriptListener wrapper = new SWTScriptListener(this);
	protected Composite main;
	
	protected void updateCurrentClient ()
	{
		if (client != null)
		{
			client.removeScriptListener(wrapper);
		}
		
		GameView inFocus = GameView.getViewInFocus();
		if (inFocus != null)
		{
			IStormFrontClient client = inFocus.getStormFrontClient();
			this.client = client;
		}
		
		if (client != null)
		{
			client.addScriptListener(wrapper);
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {
		updateCurrentClient();
		
		main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(2, false));
		main.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		Label label = new Label(main, SWT.NONE);
		label.setText("Running Scripts");
		label.setFont(JFaceResources.getBannerFont());
	}

	@Override
	public void setFocus() {
		updateCurrentClient();
	}

	
	public void scriptPaused(IScript script) {
		// TODO Auto-generated method stub
		
	}
	
	public void scriptResumed(IScript script) {
		// TODO Auto-generated method stub
		
	}
	
	public void scriptStarted(final IScript script) {
//		form.setText("");
		
		Composite scriptComposite = new Composite(main, SWT.NONE);
		scriptComposite.setLayout(new GridLayout(3, false));
		scriptComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		Label label = new Label(scriptComposite, SWT.NONE);
		label.setText(script.getName() + ": ");
		
		final Button pause = new Button(scriptComposite, SWT.PUSH);
		pause.setImage(ScribeSharedImages.getImage(ScribeSharedImages.IMG_SUSPEND));
		
		Button stop = new Button(scriptComposite, SWT.PUSH);
		stop.setImage(ScribeSharedImages.getImage(ScribeSharedImages.IMG_TERMINATE));
		
		pause.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (pause.getImage().equals(ScribeSharedImages.getImage(ScribeSharedImages.IMG_RESUME)))
				{
					pause.setImage(ScribeSharedImages.getImage(ScribeSharedImages.IMG_SUSPEND));
					script.resume();
				} else {
					pause.setImage(ScribeSharedImages.getImage(ScribeSharedImages.IMG_RESUME));
					script.suspend();
				}
			}
		});
		
		stop.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				script.stop();
			}
		});
		
		main.update();
	}
	
	public void scriptStopped(IScript script, boolean userStopped) {
		// TODO Auto-generated method stub
		
	}
}
