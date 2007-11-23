package cc.warlock.rcp.ui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacro;
import cc.warlock.rcp.ui.macros.MacroRegistry;
import cc.warlock.rcp.ui.macros.internal.CommandHistoryMacroHandler;

public class WarlockEntry implements VerifyKeyListener {

	private StyledText widget;
	private IWarlockClientViewer viewer;
	private ArrayList<IMacro> entryMacros = new ArrayList<IMacro>();
	private boolean searchMode = false;
	private StringBuffer searchText = new StringBuffer();
	private String command = "";
	
	public WarlockEntry(Composite parent, IWarlockClientViewer viewer) {
		this.viewer = viewer;
		widget = new StyledText(parent, SWT.BORDER | SWT.SINGLE);
		widget.setLayoutData(new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_END, true, false, 1, 1));
		widget.setEditable(true);
		widget.setLineSpacing(2);
		widget.addVerifyKeyListener(this);
		
		for (IMacro macro : new CommandHistoryMacroHandler().getMacros())
		{
			entryMacros.add(macro);
		}
	}
	
	public String getText() {
		return command;
	}
	
	public void setText(String text) {
		command = text;
		widget.setText(text);
		widget.setCaretOffset(command.length());
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

	public void verifyKey(VerifyEvent e) {
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
		
		//if (!widget.isFocusControl()) {
			// TODO make this a bit more robust
			if(entryCharacters.contains(e.character))
			{
				append(e.character);
				e.doit = false;
			} else if(e.character == '\b') {
				widget.invokeAction(ST.DELETE_PREVIOUS);
				widget.setCaretOffset(widget.getText().length());
				widget.setFocus();
				e.doit = false;
			}
		//}
	}
	
	public void append(char ch) {
		if(searchMode) {
			searchText.append(ch);
			ICommand searchCommand = viewer.getWarlockClient().getCommandHistory().search(searchText.toString());
			if(searchCommand != null) {
				searchText.setLength(0);
				searchText.append(searchCommand.getCommand());
			}
			setSearchText();
		} else {
			command += ch;
			widget.setText(command);
			widget.setCaretOffset(command.length());
			widget.setFocus();
		}
	}
	
	private static final char[] entryChars = new char[] {
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'.', '/', '{', '}', '<', '>', ',', '?', '\'', '"', ':', ';', '[', ']', '|', '\\', '-', '_', '+', '=',
		'~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', ' '
	};
	
	private static final ArrayList<Character> entryCharacters = new ArrayList<Character>();
	static {
		for (char c : entryChars) {
			entryCharacters.add(c);
		}
	}
	
	public void prevCommand() {
		ICommand prevCommand = viewer.getWarlockClient().getCommandHistory().prev();
		
		if(prevCommand != null)
			setText(prevCommand.getCommand());
		else
			setText("");
	}
	
	public void nextCommand() {
		ICommand nextCommand = viewer.getWarlockClient().getCommandHistory().next();
		if(nextCommand != null) {
			setText(nextCommand.getCommand());
		} else {
			setText("");
		}
	}
	
	public void searchHistory() {
		if(searchMode) {
			ICommand searchCommand = viewer.getWarlockClient().getCommandHistory().searchBefore(searchText.toString());
			if(searchCommand != null) {
				searchText.setLength(0);
				searchText.append(searchCommand.getCommand());
			}
			setSearchText();
		} else {
			searchMode = true;
			setSearchText();
		}
	}
	
	private void setSearchText() {
		widget.setText("(reverse history search)'" + searchText.toString() + "': " + command);
	}
	
	public void submit() {
		send(command);
		viewer.getWarlockClient().getCommandHistory().resetPosition();
		setText("");
		searchMode = false;
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
