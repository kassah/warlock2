/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.rcp.ui;

import java.util.ArrayList;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.internal.Command;
import cc.warlock.core.client.settings.macro.IMacro;
import cc.warlock.rcp.views.GameView;

public class WarlockEntry {

	private StyledText widget;
	private IWarlockClientViewer viewer;
	private boolean searchMode = false;
	private StringBuffer searchText = new StringBuffer();
	private String searchCommand = "";
	
	public WarlockEntry(Composite parent, IWarlockClientViewer viewer) {
		this.viewer = viewer;
		
		widget = new StyledText(parent, SWT.SINGLE); 
		widget.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1, 1));
		widget.setEditable(true);
		
		if (!Platform.getOS().equals(Platform.OS_MACOSX))
			widget.setLineSpacing(2);
		
		widget.addVerifyKeyListener(new KeyVerifier());
	}
	
	public String getText() {
		if(searchMode)
			return searchCommand;
		else
			return widget.getText();
	}
	
	public void setText(String text) {
		text = text.trim();
		widget.setText(text);
		widget.setCaretOffset(text.length());
	}
	
	public void setSelection(int start) {
		widget.setSelection(start);
	}
	
	public void addKeyListener(KeyListener listener) {
		widget.addKeyListener(listener);
	}

	public StyledText getWidget() {
		return widget;
	}
	
	protected boolean checkAndExecuteMacro (IMacro macro, int keyCode, int stateMask)
	{
		if (macro.getKeyCode() == keyCode && macro.getModifiers() == stateMask)
		{
			try {
				leaveSearchMode();
				macro.execute(viewer);
				//keyHandled = true;
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}

			return true;
		}
		return false;
	}
	
	// returns whether we processed the key or not.
	protected boolean processKey(int keyCode, int stateMask, char character) {
		//System.out.println("got char \"" + e.character + "\"");
		for (IMacro macro : viewer.getWarlockClient().getClientSettings().getAllMacros())
		{
			if (checkAndExecuteMacro(macro, keyCode, stateMask)) {
				return true;
			}
		}

		if (!widget.isFocusControl() || searchMode) {
			if(entryCharacters.contains(character))
			{
				append(character);
				if(!widget.isFocusControl())
					widget.setFocus();
				return true;
			} else if(character == '\b') {
				if(searchMode) {
					searchText.setLength(searchText.length() - 1);
					search();
				} else {
					widget.invokeAction(ST.DELETE_PREVIOUS);
				}
				if(!widget.isFocusControl())
					widget.setFocus();
				return true;
			}
		}
		return false;
	}
	
	public class KeyVerifier implements VerifyKeyListener {
		public void verifyKey(VerifyEvent e) {
			if(!e.doit)
				return;
			if(processKey(e.keyCode, e.stateMask, e.character))
				e.doit = false;
		}
	}
	
	public class KeyEventListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if(!e.doit || viewer.getWarlockClient() != GameView.getGameViewInFocus().getWarlockClient())
				return;
			if(processKey(e.keyCode, e.stateMask, e.character))
				e.doit = false;
		}
		
		public void keyReleased(KeyEvent e) {
			// Do nothing
		}
	}
	
	private void leaveSearchMode() {
		if(searchMode) {
			 searchMode = false;
			 setText(searchCommand);
			 searchCommand = "";
			 searchText.setLength(0);
		 }
	}
	
	public void append(char ch) {
		if(searchMode) {
			searchText.append(ch);
			search();
		} else {
			int offset = widget.getCaretOffset();
			widget.insert(String.valueOf(ch));
			widget.setCaretOffset(offset + 1);
		}
	}
	
	private void search() {
		ICommand foundCommand = viewer.getWarlockClient().getCommandHistory().search(searchText.toString());
		if(foundCommand != null) {
			searchCommand = foundCommand.getCommand();
		}
		setSearchText();
	}
	
	private static final ArrayList<Character> entryCharacters = new ArrayList<Character>();
	static {
		char[] entryChars = new char[] {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'.', '/', '{', '}', '<', '>', ',', '?', '\'', '"', ':', ';', '[', ']', '|', '\\', '-', '_', '+', '=',
			'~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', ' '
		};
		
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
			ICommand foundCommand = viewer.getWarlockClient().getCommandHistory().searchBefore(searchText.toString());
			if(foundCommand != null) {
				searchCommand = foundCommand.getCommand();
			}
			setSearchText();
		} else {
			searchMode = true;
			setSearchText();
		}
	}
	
	private void setSearchText() {
		widget.setText("(reverse history search)'" + searchText.toString() + "': " + searchCommand);
	}
	
	public void submit() {
		String text;
		if(searchMode) {
			text = searchCommand;
			leaveSearchMode();
		} else {
			text = widget.getText();
		}
		ICommand command = new Command(text);
		viewer.getWarlockClient().send(command);
		viewer.getWarlockClient().getCommandHistory().addCommand(command);
		viewer.getWarlockClient().getCommandHistory().resetPosition();
		setText("");
	}
	
	public void repeatLastCommand() {
		if (viewer.getWarlockClient().getCommandHistory().size() >= 1) {
			ICommand command = viewer.getWarlockClient().getCommandHistory().getLastCommand();
			viewer.getWarlockClient().send(command);
		}
	}
	
	public void repeatSecondToLastCommand() {
		if (viewer.getWarlockClient().getCommandHistory().size() >= 2)
		{
			ICommand command = viewer.getWarlockClient().getCommandHistory().getCommandAt(1);
			viewer.getWarlockClient().send(command);
		}
	}
}
