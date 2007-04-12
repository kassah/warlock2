/*
 * Created on Sep 17, 2004
 */
package com.arcaner.warlock.rcp.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.client.stormfront.IStormFrontClient;
import com.arcaner.warlock.client.stormfront.IStormFrontClientViewer;
import com.arcaner.warlock.client.stormfront.WarlockColor;
import com.arcaner.warlock.configuration.ServerSettings;
import com.arcaner.warlock.rcp.ui.WarlockCompass;
import com.arcaner.warlock.rcp.ui.WarlockText;
import com.arcaner.warlock.rcp.ui.client.SWTStormFrontClientViewer;
import com.arcaner.warlock.rcp.ui.macros.IMacro;
import com.arcaner.warlock.rcp.ui.macros.MacroFactory;
import com.arcaner.warlock.rcp.ui.style.CompassThemes;

/**
 * @author marshall
 */
public class GameView extends StreamView implements KeyListener, IStormFrontClientViewer {

	public static final String VIEW_ID = "com.arcaner.warlock.rcp.views.gameView";
	
	private static GameView firstInstance;
	private static boolean firstInstanceIsUsed = false;
	private static ArrayList<GameView> openViews = new ArrayList<GameView>();
	private static GameView viewInFocus;
	
	protected Text entry;
	protected WarlockCompass compass;
	
	protected SWTStormFrontClientViewer wrapper;
//	protected ViewEvents viewer;
	private Font normalFont = JFaceResources.getDefaultFont();
	
	public GameView () {
		if (firstInstance == null)
			firstInstance = this;
		
		wrapper = new SWTStormFrontClientViewer(this);
//		viewer = new ViewEvents(client);
		openViews.add(this);
		setStreamName(IWarlockClient.DEFAULT_STREAM_NAME);
	}
	
	public static Collection<GameView> getOpenGameViews ()
	{
		return openViews;
	}
	
	public static GameView getViewInFocus ()
	{
		return viewInFocus;
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
	
//	private class ViewEvents implements IStormFrontClientViewer {
//		
//		private SWTStormFrontClientViewer viewerWrapper;
//		
//		public ViewEvents (IStormFrontClient client)
//		{
//			super(client);
//			
//			viewerWrapper = new SWTStormFrontClientViewer(this);
//		}
//		
//		public void append (IStream stream, String toAppend, IWarlockStyle style) {
//			GameView.this.append(stream, toAppend, style);
//		}
//		
//		public void echo (IStream stream, String text, IWarlockStyle style) {
//			GameView.this.echo(stream, text, style);
//		}
//		
//		public void setViewerTitle(String title) {
//			GameView.this.setViewerTitle(title);
//		}
//		
//		public String getCurrentCommand ()
//		{
//			return GameView.this.getCurrentCommand();
//		}
//		
//		public void setClient (IStormFrontClient client)
//		{
//			this.client = client;
//			
//			client.addViewer(viewerWrapper);
//		}
//
//		public SWTStormFrontClientViewer getViewerWrapper() {
//			return viewerWrapper;
//		}
//		
//		public void loadServerSettings (ServerSettings settings)
//		{
//			GameView.this.loadServerSettings(settings);
//		}
//		
//		public void setCurrentCommand(String command) {
//			entry.setText(command);
//		}
//	}
	
	private static String generateUniqueId () {
		return new Random().nextInt() + "";
	}
	
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		text.addFocusListener(new FocusListener () { 
			public void focusGained(FocusEvent e) {
				entry.setFocus();
			}
			
			public void focusLost(FocusEvent e) {}
		});
		
		text.append(
			"Hello, and welcome to Warlock 2!\n" + 
			"To get started, you can connect to your play.net account by opening the \"Connect\" menu, and pressing \"New Connection\".\n" +
			"Good luck, and happy hunting!\n\n" +
			"Copyright (c) 2001-2007 " + WarlockText.OBJECT_HOLDER);
		
		text.addLink("http://warlock.sf.net", "The Warlock Team");
		
		entry = new Text(mainComposite, SWT.BORDER);
		entry.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false, 1, 1));
		entry.addKeyListener(this);
		
		compass = new WarlockCompass(text, CompassThemes.getCompassTheme("small"));
		text.setBackgroundMode(SWT.INHERIT_DEFAULT);
	}
	
	public void setFocus() {
		entry.setFocus();
		
		viewInFocus = this;
	}
	
	private Color createColor (WarlockColor color)
	{
		return new Color(getSite().getShell().getDisplay(), color.getRed(), color.getGreen(), color.getBlue());
	}
	
	public void loadServerSettings (final ServerSettings settings)
	{
		WarlockColor bg = settings.getColorSetting(ServerSettings.ColorType.MainWindow_Background);
		WarlockColor fg = settings.getColorSetting(ServerSettings.ColorType.MainWindow_Foreground);
		
		String fontFace = settings.getStringSetting(ServerSettings.StringType.MainWindow_FontFace);
		int fontSize = settings.getIntSetting(ServerSettings.IntType.MainWindow_FontSize);
		
		normalFont = fontFace == null ? JFaceResources.getDefaultFont() : new Font(getSite().getShell().getDisplay(), fontFace, fontSize, SWT.NONE);
		text.setFont(normalFont);
		
		WarlockColor entryBG = settings.getColorSetting(ServerSettings.ColorType.CommandLine_Background);
		WarlockColor entryFG = settings.getColorSetting(ServerSettings.ColorType.CommandLine_Foreground);
		entry.setForeground(createColor(entryFG.equals(WarlockColor.DEFAULT_COLOR) ? fg  : entryFG));
		entry.setBackground(createColor(entryBG.equals(WarlockColor.DEFAULT_COLOR) ? bg : entryBG));
	}
	
	public void setViewerTitle(String title) {
		setPartName(title);
	}
	
	public String getCurrentCommand ()
	{
		return GameView.this.entry.getText();
	}
	
	public void setCurrentCommand(String command) {
		GameView.this.entry.setText(command);
	}
	
	public void setStormFrontClient(IStormFrontClient client) {
		this.client = client;
		
		setStream(client.getDefaultStream());
		
		text.setText("");
		
		compass.setCompass(client.getCompass());
		
		if (BarsView.getDefault() != null)
			BarsView.getDefault().init(client);
		
		for (StreamView streamView : StreamView.getOpenViews())
		{
			if (streamView != this) {
				streamView.setClient(client);
			}
		}
	}
	
	public IWarlockClient getWarlockClient() {
		return client;
	}
	
	public IStormFrontClient getStormFrontClient() {
		return client;
	}

	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		Collection<IMacro> macros = MacroFactory.instance().getMacros();
		
		for (IMacro macro : macros)
		{
			if (macro.getKeyCode() == e.keyCode)
			{
				String result = macro.executeCommand(wrapper);
				if (result != null)
				{
					entry.setText(result);
				}
//				e.doit = false;
				break;
			}
		}
		
	}
}
