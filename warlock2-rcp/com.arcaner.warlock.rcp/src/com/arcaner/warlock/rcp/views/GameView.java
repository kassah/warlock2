/*
 * Created on Sep 17, 2004
 */
package com.arcaner.warlock.rcp.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.IWarlockClientViewer;
import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.client.stormfront.IStormFrontClientListener;
import com.arcaner.warlock.rcp.ui.client.SWTWarlockClientViewer;
import com.arcaner.warlock.rcp.ui.macros.IMacro;
import com.arcaner.warlock.rcp.ui.macros.MacroFactory;

/**
 * @author marshall
 */
public class GameView extends ViewPart implements KeyListener {

	public static final String VIEW_ID = "com.arcaner.warlock.views.gameView";
	
	private static GameView firstInstance;
	private static boolean firstInstanceIsUsed = false;
	private static ArrayList<GameView> openViews = new ArrayList<GameView>();
	
	protected StyledText text;
	protected Text entry;

	protected IStormFrontClient client;
	protected ClientViewer viewer;
	
	public GameView() {
		if (firstInstance == null)
			firstInstance = this;
		
		viewer = new ClientViewer();
		openViews.add(this);
	}
	
	public static GameView createNext () {
		if (firstInstance != null)	
		{
			if (!firstInstanceIsUsed)
			{
				firstInstanceIsUsed = true;
				return firstInstance;
			}
			
			else {
				IWorkbenchPage page = firstInstance.getSite().getPage();
				try {
					GameView nextInstance = (GameView) page.showView(VIEW_ID, generateUniqueId(), IWorkbenchPage.VIEW_ACTIVATE);
					return nextInstance;
				} catch (PartInitException e) {
					e.printStackTrace();
				}	
			}
		}	
		
		return null;
	}
	
	private static String generateUniqueId () {
		return new Random().nextInt() + "";
	}
	
	public void createPartControl(Composite parent) {
		
		Composite top = new Composite (parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		top.setLayout(layout);
		
		text = new StyledText(top, SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		text.setEditable(false);
		text.setWordWrap(true);
		text.setBackground(new Color(text.getDisplay(), 25, 25, 50));
		text.setForeground(new Color(text.getDisplay(), 240, 240, 255));
		text.addFocusListener(new FocusListener () { 
			public void focusGained(FocusEvent e) {
				entry.setFocus();
			}
			
			public void focusLost(FocusEvent e) {}
		});
		
//		Composite entryComposite = new Composite(top, SWT.NONE);
//		entryComposite.setLayout(new GridLayout(1, false));
//		
		entry = new Text(top, SWT.BORDER);
		entry.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false, 1, 1));
		//entry.setLayoutData(new RowData(250, 15));
		entry.addKeyListener(this);
		

		
//		new Label(barComposite, SWT.NONE).setText("health");
//		new Label(barComposite, SWT.NONE).setText("mana");
//		new Label(barComposite, SWT.NONE).setText("fatigue");
//		new Label(barComposite, SWT.NONE).setText("spirit");
		

	}
	
	public void setFocus() {
		entry.setFocus();
		
		if (client != null)
		{
			for (IStormFrontClientListener listener : client.getStormFrontClientListeners())
			{
				listener.setActiveClient(client);
			}
		}
	}
	
	public IWarlockClientViewer getViewer() {
		return viewer;
	}
	
	private class ClientViewer extends SWTWarlockClientViewer
	{
		public void handleAppend(String toAppend) {
			text.append(toAppend);
			int length = text.getContent().getCharCount();		
			if (text.getCaretOffset() < length) {
				text.setCaretOffset(length);
				text.showSelection();
			}
		}
	
		public void handleEcho(String text) {
			// A hacked style range, for now. This was mainly so we could get an idea on how to do it
			// once we implement highlight prefs, etc.
			
			StyleRange range = new StyleRange();
			range.background = new Color(getSite().getShell().getDisplay(), 0x30, 0x30, 0x30);
			range.foreground = new Color(getSite().getShell().getDisplay(), 255, 255, 255);
			range.fontStyle = SWT.BOLD;
			range.start = GameView.this.text.getCharCount();
			range.length = text.length();
			
			GameView.this.text.append(text);
			GameView.this.text.setStyleRange(range);
		}
		
		public void handleSetViewerTitle(String title) {
			setPartName(title);
		}
		
		public IWarlockClient getWarlockClient () {
			return GameView.this.client;
		}
		
		public void setWarlockClient(IWarlockClient client) {
			GameView.this.client = (IStormFrontClient) client;
		}
		
		public String getCurrentCommand ()
		{
			return GameView.this.entry.getText();
		}
	}
	
	public void keyPressed(KeyEvent e) {

	}
	
	public IWarlockClient getClient() {
		return client;
	}
	
	public void keyReleased(KeyEvent e) {
		Collection<IMacro> macros = MacroFactory.instance().getMacros();
		
		for (IMacro macro : macros)
		{
			if (macro.getKeyCode() == e.keyCode)
			{
				String result = macro.executeCommand(viewer);
				if (result != null)
				{
					entry.setText(result);
				}
				
				break;
			}
		}
		
	}
}
