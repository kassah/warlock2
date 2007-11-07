package cc.warlock.rcp.ui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacro;
import cc.warlock.rcp.ui.macros.MacroRegistry;
import cc.warlock.rcp.ui.macros.internal.CommandHistoryMacroHandler;

public class WarlockEntry implements KeyListener {

	private StyledText widget;
	private IWarlockClientViewer viewer;
	private ArrayList<IMacro> entryMacros = new ArrayList<IMacro>();
	
	public WarlockEntry(Composite parent, IWarlockClientViewer viewer) {
		this.viewer = viewer;
		widget = new StyledText(parent, SWT.BORDER | SWT.SINGLE);
		widget.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false, 1, 1));
		widget.setEditable(true);
		widget.setLineSpacing(5);
		widget.setIndent(5);
		widget.addKeyListener(this);
		
		for (IMacro macro : new CommandHistoryMacroHandler().getMacros())
		{
			entryMacros.add(macro);
		}
	}
	
	public String getText() {
		return widget.getText();
	}
	
	public void setText(String text) {
		widget.setText(text);
		widget.setCaretOffset(widget.getText().length());
	}
	
	public void setSelection(int start) {
		widget.setSelection(start);
	}
	
	public void addKeyListener(KeyListener listener) {
		widget.addKeyListener(listener);
	}
	
	protected boolean isKeypadKey (int code) {
		return (code == SWT.KEYPAD_0 || code == SWT.KEYPAD_1 || code == SWT.KEYPAD_2
				|| code == SWT.KEYPAD_3 || code == SWT.KEYPAD_4 || code == SWT.KEYPAD_5
				|| code == SWT.KEYPAD_6 || code == SWT.KEYPAD_7 || code == SWT.KEYPAD_8
				|| code == SWT.KEYPAD_9 || code == SWT.KEYPAD_ADD || code == SWT.KEYPAD_CR
				|| code == SWT.KEYPAD_DECIMAL || code == SWT.KEYPAD_DIVIDE || code == SWT.KEYPAD_EQUAL
				|| code == SWT.KEYPAD_EQUAL || code == SWT.KEYPAD_MULTIPLY || code == SWT.KEYPAD_SUBTRACT);
	}

	public StyledText getWidget() {
		return widget;
	}

	public void keyPressed(KeyEvent e) {
		//keyHandled = false;
		if (e.stateMask == 0 && entryCharacters.contains(e.character) && !isKeypadKey(e.keyCode) && widget.isFocusControl()) return;
		if (!e.doit) return;
		//e.doit = true;
		
		if(widget.isFocusControl()) {
			for (IMacro macro : entryMacros)
			{
				if (macro.getKeyCode() == e.keyCode && macro.getModifiers() == e.stateMask)
				{
					 try {
						macro.execute(viewer);
						//keyHandled = true;
					} catch (Exception ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					
					e.doit = false;
					return;
				}
			}
		}
		
		for (IMacro macro : MacroRegistry.instance().getMacros())
		{
			if (macro.getKeyCode() == e.keyCode && macro.getModifiers() == e.stateMask)
			{
				 try {
					macro.execute(viewer);
					//keyHandled = true;
				} catch (Exception ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				
				e.doit = false;
				return;
			}
		}
		
		if (!widget.isFocusControl() && entryCharacters.contains(e.character))
		{
			append(e.character);
			e.doit = false;
		}
	}
	
	public void append(char ch) {
		widget.append(ch+"");
		widget.setCaretOffset(widget.getText().length());
		widget.setFocus();
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

	}
	
	public void prevCommand() {
		ICommand command = viewer.getWarlockClient().getCommandHistory().prev();
		
		if(command != null)
			setText(command.getCommand());
		else
			setText("");
	}
	
	public void nextCommand() {
		ICommand command = viewer.getWarlockClient().getCommandHistory().next();
		if(command != null) {
			setText(command.getCommand());
		} else {
			setText("");
		}
	}
	
	public void submit() {
		String command = widget.getText();
		if (!command.equals(""))
		{
			send(command);
			setText("");
		}
	}
	
	private void send(String command) {
		viewer.getWarlockClient().send(command);
		viewer.getWarlockClient().getCommandHistory().addCommand(command);
	}
	
	public void repeatLastCommand() {
		ICommand command = viewer.getWarlockClient().getCommandHistory().getLastCommand();
		send(command.getCommand());
	}
	
	public void repeatSecondToLastCommand() {	
		if (viewer.getWarlockClient().getCommandHistory().size() >= 2)
		{
			ICommand command = viewer.getWarlockClient().getCommandHistory().getCommandAt(1);
			send(command.getCommand());
		}
	}
}
