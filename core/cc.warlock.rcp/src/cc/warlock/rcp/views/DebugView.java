package cc.warlock.rcp.views;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.core.network.IConnection;
import cc.warlock.core.network.IConnectionListener;
import cc.warlock.rcp.application.WarlockApplication;
import cc.warlock.rcp.ui.WarlockText;
import cc.warlock.rcp.ui.network.SWTConnectionListenerAdapter;

public class DebugView extends ViewPart implements IConnectionListener {

	protected WarlockText console;
	protected Text entry;
	protected Button copyAll;
	
	public static final String VIEW_ID = "cc.warlock.rcp.views.DebugView";
	
	protected IConnection connection;
	
	public void setConnection (IConnection connection)
	{
		this.connection = connection;
		
		connection.addConnectionListener(new SWTConnectionListenerAdapter(this));
	}
	
	public void debug (String message)
	{
		if (WarlockApplication.instance().inDebugMode()) {
			console.append(message);
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));

		copyAll = new Button(main, SWT.PUSH);
		copyAll.setText("Copy All");
		
		console = new WarlockText(main, SWT.V_SCROLL | SWT.H_SCROLL);
		console.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		console.setScrollDirection(SWT.DOWN);
		
		copyAll.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				console.selectAll();
				console.copy();
			}
		});
		
		entry = new Text(main, SWT.BORDER);
		entry.addKeyListener(new KeyAdapter () {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR)
				{
					sendRawText();
				}
			}
		});
		entry.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
	}
	
	protected void sendRawText ()
	{
		String toSend = entry.getText() + "\n";
		try {
			connection.send(toSend.getBytes());
			entry.setText("");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void setFocus() {
		console.setFocus();
	}
	
	public void connected(IConnection connection) {
		debug ("connected");
	}
	
	public void dataReady(IConnection connection, String line) {
		debug (line);
	}
	
	public void disconnected(IConnection connection) {
		debug ("disconnected");
	}

}
