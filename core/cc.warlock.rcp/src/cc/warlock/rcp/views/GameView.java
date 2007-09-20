/*
 * Created on Sep 17, 2004
 */
package cc.warlock.rcp.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.internal.Command;
import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.ui.WarlockCompass;
import cc.warlock.rcp.ui.WarlockText;
import cc.warlock.rcp.ui.client.SWTWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacro;
import cc.warlock.rcp.ui.macros.MacroRegistry;
import cc.warlock.rcp.ui.style.CompassThemes;

/**
 * @author marshall
 */
public abstract class GameView extends StreamView implements KeyListener, IWarlockClientViewer {

	protected static GameView firstInstance;
	protected static boolean firstInstanceIsUsed = false;
	protected static ArrayList<GameView> openViews = new ArrayList<GameView>();
	protected static ArrayList<IGameViewFocusListener> focusListeners = new ArrayList<IGameViewFocusListener>();
	protected static GameView viewInFocus;
	
	protected WarlockText text;
	protected StyledText entry;
	protected WarlockCompass compass;
	protected ICommand currentCommand;
	protected SWTWarlockClientViewer wrapper;
	
	public GameView () {
		if (firstInstance == null) {
			firstInstance = this;
		}
		
		currentCommand = new Command("", new Date());
		openViews.add(this);
		wrapper = new SWTWarlockClientViewer(this);
		
		setStreamName(IWarlockClient.DEFAULT_STREAM_NAME);
	}
	
	public static void addGameViewFocusListener (IGameViewFocusListener listener)
	{
		focusListeners.add(listener);
	}
	
	public static void removeGameViewFocusListener (IGameViewFocusListener listener)
	{
		if (focusListeners.contains(listener))
			focusListeners.remove(listener);
	}
	
	public static Collection<GameView> getOpenGameViews ()
	{
		return openViews;
	}
	
	public static GameView getViewInFocus ()
	{
		return viewInFocus;
	}
	
	
	public static GameView createNext (String viewId) {
//		if (firstInstance != null)	
//		{
//			if (!firstInstanceIsUsed)
//			{
//				firstInstanceIsUsed = true;
//				viewInFocus = firstInstance;
//				return firstInstance;
//			}
//			
//			else {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IViewPart part = page.showView(viewId, generateUniqueId(), IWorkbenchPage.VIEW_ACTIVATE);
					// if there's an error in creating the view, we want to know about it.. don't cast unless we know it's not an errorviewpart
					if (part instanceof GameView)
					{
						GameView nextInstance = (GameView) part;
						viewInFocus = nextInstance;
						
						if (ConnectionView.closeAfterConnect)
						{
							part = page.findView(ConnectionView.VIEW_ID);
							if (part != null)
								page.hideView(part);
						}
						
						return nextInstance;
					}
					
				} catch (PartInitException e) {
					e.printStackTrace();
				}	
//			}
//		}	
//		
		return null;
	}
	
	protected static String generateUniqueId () {
		return new Random().nextInt() + "";
	}
	
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		this.client = Warlock2Plugin.getDefault().getCurrentClient();
		this.text = getTextForClient(this.client);
		book.showPage(this.text.getTextWidget());
		
		text.setLineLimit(2000);
		text.setScrollDirection(SWT.DOWN);
		text.addKeyListener(this);
		
		entry = new StyledText(mainComposite, SWT.BORDER | SWT.SINGLE);
		entry.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false, 1, 1));
		entry.setEditable(true);
		entry.setLineSpacing(5);
		entry.setIndent(5);
		
		compass = new WarlockCompass(text, CompassThemes.getCompassTheme("small"));
		text.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		entry.addModifyListener(new ModifyListener () {
			public void modifyText(ModifyEvent e) {
				((Command)currentCommand).setCommand(entry.getText());
			}
		});
		entry.addKeyListener(this);
		
	}
	
	public void setFocus() {
		entry.setFocus();
		
		viewInFocus = this;
		for (IGameViewFocusListener listener : focusListeners)
		{
			listener.gameViewFocused(this);
		}
	}
	
	protected Image createCaretImage (int width, Color foreground)
	{
		PaletteData caretPalette = new PaletteData(new RGB[] {
				new RGB(0, 0, 0), new RGB(255, 255, 255) });
		
		int widthOffset = width - 1;
		ImageData imageData = new ImageData(4 + widthOffset, entry
				.getLineHeight(), 1, caretPalette);
		Display display = entry.getDisplay();
		Image bracketImage = new Image(display, imageData);
		GC gc = new GC(bracketImage);
		gc.setForeground(foreground);
		gc.setLineWidth(1);
		// gap between two bars of one third of the height
		// draw boxes using lines as drawing a line of a certain width produces
		// rounded corners.
		for (int i = 0; i < width; i++) {
			gc.drawLine(i, 0, i, imageData.height - 1);
		}

		gc.dispose();

		return bracketImage;
	}

	protected Caret createCaret (int width, Color foreground) {
		Caret caret = new Caret(entry, SWT.NULL);
		Image image = createCaretImage(width, foreground);
		
		if (image != null)
			caret.setImage(image);
		else
			caret.setSize(width, entry.getLineHeight());

		caret.setFont(entry.getFont());

		return caret;
	}
	
	public ICommand getCurrentCommand ()
	{
		return GameView.this.currentCommand;
	}
	
	public void setCurrentCommand(ICommand command) {
		GameView.this.currentCommand = command;
		GameView.this.entry.setText(command.getCommand());
		GameView.this.entry.setSelection(command.getCommand().length());
	}
	
	public void setClient(IWarlockClient client) {
		this.client = client;
		book.showPage(getTextForClient(client).getTextWidget());
		
		setMainStream(client.getDefaultStream());
		client.addViewer(wrapper);
		
//		for (StreamView streamView : StreamView.getOpenViews())
//		{
//			if (!(streamView instanceof GameView)) {
//				streamView.setClient(client);
//			}
//		}
	}
	
	public IWarlockClient getWarlockClient() {
		return client;
	}

	protected boolean isKeypadKey (int code) {
		return (code == SWT.KEYPAD_0 || code == SWT.KEYPAD_1 || code == SWT.KEYPAD_2
				|| code == SWT.KEYPAD_3 || code == SWT.KEYPAD_4 || code == SWT.KEYPAD_5
				|| code == SWT.KEYPAD_6 || code == SWT.KEYPAD_7 || code == SWT.KEYPAD_8
				|| code == SWT.KEYPAD_9 || code == SWT.KEYPAD_ADD || code == SWT.KEYPAD_CR
				|| code == SWT.KEYPAD_DECIMAL || code == SWT.KEYPAD_DIVIDE || code == SWT.KEYPAD_EQUAL
				|| code == SWT.KEYPAD_EQUAL || code == SWT.KEYPAD_MULTIPLY || code == SWT.KEYPAD_SUBTRACT);
	}
	
	protected boolean keyHandled = false;
	public void keyPressed(KeyEvent e) {
		keyHandled = false;
		if (e.stateMask == 0 && entryCharacters.contains(e.character) && !isKeypadKey(e.keyCode)) return;
		
		Collection<IMacro> macros = MacroRegistry.instance().getMacros();
		e.doit = true;
		
		for (IMacro macro : macros)
		{
			if (macro.getKeyCode() == e.keyCode && macro.getModifiers() == e.stateMask)
			{
				 try {
					macro.execute(wrapper);
					keyHandled = true;
				} catch (Exception ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				
				e.doit = false;
				break;
			}
		}
	}
	
	private static final char[] entryChars = new char[] {
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'.', '/', '{', '}', '<', '>', ',', '?', '\'', '"', ':', ';', '[', ']', '|', '\\', '-', '_', '+', '=',
		'~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')'
	};
	private static final ArrayList<Character> entryCharacters = new ArrayList<Character>();
	static {
		for (char c : entryChars) {
			entryCharacters.add(c);
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if (!entry.isFocusControl() && !keyHandled) {
			
			if (entryCharacters.contains(e.character))
			{
				entry.append(e.character+"");
				entry.setCaretOffset(entry.getText().length());
				entry.setFocus();
			}
		}
	}
	
	@Override
	public void dispose() {
		if (client != null && client.getConnection() != null && client.getConnection().isConnected())
		{
			try {
				client.getConnection().disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		super.dispose();
	}
}
