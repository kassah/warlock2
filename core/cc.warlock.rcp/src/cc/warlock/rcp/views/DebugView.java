package cc.warlock.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cc.warlock.network.IConnection;
import cc.warlock.network.IConnectionListener;
import cc.warlock.rcp.application.WarlockApplication;

public class DebugView extends ViewPart implements IConnectionListener {

	protected StyledText console;
	protected Button copyAll;
	
	public static final String VIEW_ID = "cc.warlock.rcp.views.DebugView";
	private static DebugView _instance;
	
	public DebugView ()
	{
		_instance = this;
	}
	
	public static DebugView instance()
	{
		return _instance;
	}
	
	public void debug (String message)
	{
		if (WarlockApplication.instance().inDebugMode()) {
			console.append(message);
			scrollToBottom();
		}
	}
	
	private void scrollToBottom ()
	{
		int length = console.getContent().getCharCount();		
		if (console.getCaretOffset() < length) {
			console.setCaretOffset(length);
			console.showSelection();
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));

		copyAll = new Button(main, SWT.PUSH);
		copyAll.setText("Copy All");
		
		console = new StyledText(main, SWT.V_SCROLL | SWT.H_SCROLL);
		console.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		copyAll.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				console.selectAll();
				console.copy();
			}
		});
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
